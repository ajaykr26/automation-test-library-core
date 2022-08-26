function() {
    karate.configure('ssl', {trustAll: true});

    var config = {
        env: "UAT",
        CORE_UTILS: "classpath:utils/",
        CORE_QUERY_UTILS: "classpath:utils/queries/",
        CORE_TESTSPEC_UTILS: "classpath:utils/testSpecs/",
        CORE_DB_UTILS: "classpath:utils/dataBase/",
        CORE_REPORTING_UTILS: "classpath:utils/reporting/",
        CORE_GET_REPORT_INFO: "classpath:utils/getReportInfo.js"
    };

    var env = karate.env;
    config.env = env;
    return config;
}