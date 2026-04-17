include 'tasks.groovy'

groups {
    group('24214') {
        student {
            github = 'NikRo12'
            name   = 'Romanenko Nikita'
            repo   = 'https://github.com/NikRo12/OOP'
        }
    }

    group('24213') {
        student {
            github = 'ivanov-ivan'
            name   = 'Ivanov Ivan Ivanovich'
            repo   = 'https://github.com/ivanov-ivan/OOP'
        }
        student {
            github = 'petrov-petr'
            name   = 'Petrov Petr Petrovich'
            repo   = 'https://github.com/petrov-petr/OOP'
        }
    }
}

checkPoints {
    checkPoint('CP1') { date = '2026-03-15' }
    checkPoint('Final') { date = '2026-05-17' }
}
