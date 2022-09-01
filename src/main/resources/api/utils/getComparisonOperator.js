function getComparisonOperator(comparisonOperator) {
    switch(comparisonOperator) {
        case 'Equal To':
            return '=='

        case 'Not Equal To':
            return '!='

        case 'Greater Than':
            return '>'

        case 'Less Than':
            return '<'

        case 'Greater Than or Equal To':
            return '>='

        case 'Less Than or Equal To':
            return '<='

        default:
            karate.log('unsupported comparison operator ' + comparisonOperator + 'defaulting to equal case')
            return '==';
    }

}