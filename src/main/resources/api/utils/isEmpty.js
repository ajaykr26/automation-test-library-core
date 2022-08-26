function isEmpty(testSpecArr) {
    if (typeof testSpecArr === "undefined"
        ||testSpecArr.length < 1
        ){
        return true;
    }
    else {
        return false;
    }
}