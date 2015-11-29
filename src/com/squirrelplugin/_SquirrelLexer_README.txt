After generation from BNF, you should fix the new lines behavior. They should work as SEMICOLON_SYNTHETIC in case if
new line goes after literals, identifiers and all kinds of braces. The great example of how it's done can be found in
GO idea plugin (in case if current implementation will be gone by some reason).