include 'semester.groovy'

assignments {
    assign {
        students = ['NikRo12', 'ivanov-ivan', 'petrov-petr']
        tasks    = ['Task_2_1_1']
    }
}

settings {
    testTimeout = 120
    gradeThresholds {
        excellent    = 85
        good         = 70
        satisfactory = 55
    }
    bonusPoints {
        student('NikRo12') {
            task('Task_2_1_1') { bonus = 1 }
        }
    }
}
