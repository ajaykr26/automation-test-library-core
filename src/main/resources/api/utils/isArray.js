function isArray(obj) {
    if (karate.sizeOf(obj) > 0){
        return Object.prototype.toString.call(obj) === '[object net.minidev.json.JSONArray]';
    }
    else {
        return false;
    }
}