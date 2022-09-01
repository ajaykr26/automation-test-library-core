function getJsonPathQuery(attributeVal, valToSearchBy){
    var attributeType = typeof attributeVal;

    if(attributeType === 'string') {
            return "'" + valToSearchBy "'";
        } else {
            return valToSearchBy;
        }
}