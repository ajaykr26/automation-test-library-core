function getReportInfo(status){
    var reportInfo = {
        "requestURI" : karate.prevRequest.uri,
        "requestHeaders" : karate.prevRequest.headers,
        "requestBody" : karate.prevRequest.body == null ? null : new java.lang.String(karate.prevRequest.body, 'utf-8')
    };
    return reportInfo
}