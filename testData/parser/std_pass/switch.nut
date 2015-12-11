switch(a){}
switch(a){ case 1: }
switch(a){ case 1: default: }
switch(a){ case 1:
case 2: default: }

switch(a)
{
    case "integer":
    case "float":
        return "is a number";
    case "table":
    case "array":
        return "is a container";
    default:
        return "is other stuff"
}