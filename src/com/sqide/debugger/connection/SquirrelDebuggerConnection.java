package com.sqide.debugger.connection;

import com.intellij.concurrency.AsyncFutureFactory;
import com.intellij.concurrency.AsyncFutureResult;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.sqide.debugger.connection.events.SquirrelDebuggerEvent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class SquirrelDebuggerConnection {
    private static final int RECEIVE_TIMEOUT = 50;
    private static final int RETRIES_ON_TIMEOUT = 10;

    @NotNull
    private final SquirrelDebuggerEventListener myEventListener;
    private final String myDebugIp;
    private final int myDebugPort;
    @NotNull
    private AtomicBoolean myStopped = new AtomicBoolean(false);
    private final Queue<String> myCommandsQueue = new LinkedList<String>();
    public static final Logger LOG = Logger.getInstance(SquirrelDebuggerConnection.class);

    public SquirrelDebuggerConnection(@NotNull SquirrelDebuggerEventListener eventListener, String debugIp, int debugPort) throws ExecutionException {
        myEventListener = eventListener;
        myDebugIp = debugIp;
        myDebugPort = debugPort;

        LOG.debug("Starting debugger server.");
        try {
            runDebuggerServer().get();
        }
        catch (Throwable e) {
            throw new ExecutionException("Failed to start debugger server with error: " + e.getMessage());
        }
    }

    public void stop() {
        addCommand("tr");
        myStopped.set(true);
    }

    public boolean isStopped() {
        return myStopped.get();
    }

    public void setBreakpoint(String shortFilePath, int line) {
        addCommand("ab:" + line + ":" + shortFilePath);
    }

    public void removeBreakpoint(String shortFilePath, int line) {
        addCommand("rb:" + line + ":" + shortFilePath);
    }

    public void debuggerReady() {
        addCommand("rd");
    }

    public void stepOver() {
        addCommand("so");
    }

    public void stepInto() {
        addCommand("si");
    }

    public void stepOut() {
        addCommand("sr");
    }

    public void resume() {
        addCommand("go");
    }

    private void addCommand(String command) {
        synchronized (myCommandsQueue) {
            myCommandsQueue.add(command);
        }
    }

    @NotNull
    private Future<Integer> runDebuggerServer() {
        final AsyncFutureResult<Integer> portFuture = AsyncFutureFactory.getInstance().createAsyncFutureResult();
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                runDebuggerServerImpl(portFuture);
            }
        });
        return portFuture;
    }

    private void runDebuggerServerImpl(@NotNull AsyncFutureResult<Integer> portFuture) {
        try {
            LOG.debug("Opening a socket.");
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(myDebugIp, myDebugPort), 30000);
                try {
                    portFuture.set(socket.getLocalPort());
                    LOG.debug("Connecting to port " + socket.getLocalPort() + ".");
                    LOG.debug("Debugger connected, closing the server socket.");
                    myEventListener.debuggerStarted();
                    LOG.debug("Starting send/receive loop.");
                    serverLoop(socket);
                    int i = 3;
                }
                finally {
                    InputStream is = socket.getInputStream();
                    socket.shutdownOutput();
                    while (is.read() >= 0) ;
                    socket.shutdownInput();

//                    socket.shutdownOutput();
//                    socket.shutdownInput();
                    myStopped.set(true);
                }
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                socket.close();
                myEventListener.debuggerStopped();
            }
        }
        catch (Exception th) {
            if (!portFuture.isDone()) {
                portFuture.setException(th);
            }
            else {
                LOG.debug(th);
            }
        }
    }

    private void serverLoop(@NotNull Socket socket) throws IOException {
        socket.setSoTimeout(RECEIVE_TIMEOUT);

        while (!isStopped()) {
            if (!isStopped()) {
                receiveMessage(socket);
            }
            if (!isStopped()) {
                sendMessages(socket);
            }
        }
    }

    private void sendMessages(@NotNull Socket socket) throws SocketException {
        synchronized (myCommandsQueue) {
            while (!myCommandsQueue.isEmpty()) {
                String message = myCommandsQueue.remove();
                LOG.debug("Sending message: " + message);
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(message);
                }
                catch (SocketException e) {
                    throw e;
                }
                catch (IOException e) {
                    LOG.debug(e);
                }
            }
        }
    }

    private void receiveMessage(@NotNull Socket socket) throws SocketException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = in.readLine();

            LOG.debug("Message received: " + message);

            SquirrelDebuggerEvent event = SquirrelDebuggerEvent.create(message);
            boolean messageRecognized = event != null;
            if (messageRecognized) {
                event.process(this, myEventListener);
            }
            LOG.debug("Message processed: " + messageRecognized);
        }
        catch (SocketException e) {
            throw e;
        }
        catch (IOException e) {
            LOG.debug(e);
        }
    }
}
