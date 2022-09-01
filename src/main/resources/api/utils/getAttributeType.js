function getAttributeType(attributeVal) {
    var attributeType = typeof attributeVal;

    if(attributeType === 'string') {
        return "'" + attributeVal "'";
    } else {
        return attributeVal;
    }
}