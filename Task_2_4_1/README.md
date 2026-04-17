# OOP Checker

Консольное приложение для автоматической проверки лабораторных работ по ООП.
Собирает статистику по GitHub-репозиториям студентов на основе Groovy DSL конфига.

## Архитектура

```
oop-checker/
├── src/main/java/ru/nsu/oopchecker/
│   ├── Main.java                      # Точка входа (аналог gradle-wrapper)
│   ├── model/                         # Java доменная модель
│   │   ├── Task.java                  # Задача курса
│   │   ├── Student.java               # Студент
│   │   ├── Group.java                 # Учебная группа
│   │   ├── CheckPoint.java            # Контрольная точка
│   │   ├── AssignmentEntry.java       # Задание на проверку
│   │   ├── TaskResult.java            # Результат проверки задачи
│   │   ├── StudentResult.java         # Агрегат результатов студента
│   │   └── GradeConfig.java           # Настройки оценивания
│   ├── dsl/                           # Загрузчик DSL
│   │   ├── OopCheckerConfig.java      # Корневой объект конфигурации
│   │   └── ConfigLoader.java          # Загружает Groovy-скрипт
│   ├── checker/                       # Логика проверки
│   │   ├── GitManager.java            # git clone/pull через консоль
│   │   ├── BuildManager.java          # Компиляция, тесты, документация, стиль
│   │   ├── ScoreCalculator.java       # Подсчёт баллов с учётом дедлайнов
│   │   └── CheckRunner.java           # Оркестратор всего процесса
│   └── report/
│       └── HtmlReporter.java          # Генерация HTML-отчёта
└── src/main/groovy/ru/nsu/oopchecker/dsl/
    └── OopCheckerDslDelegate.groovy   # Сам DSL (написан на Groovy)
```

## Жизненный цикл конфигов

| Файл              | Как часто меняется | Содержимое                      |
|-------------------|--------------------|---------------------------------|
| `tasks.groovy`    | Раз в год          | Список задач, дедлайны          |
| `semester.groovy` | Раз в семестр      | Группы, студенты, контр. точки  |
| `oop_checker.groovy` | Каждая проверка | Задания, доп. баллы, настройки  |

## Формат DSL

### tasks.groovy (долгоживущий)
```groovy
tasks {
    task('Task_1_1_1') {
        title        = 'Heapsort'
        maxScore     = 100
        softDeadline = '2024-10-06'
        hardDeadline = '2024-10-20'
    }
}
```

### semester.groovy (среднеживущий)
```groovy
include 'tasks.groovy'   // импорт долгоживущего конфига

groups {
    group('NSU-21215') {
        student {
            github = 'ivanov-ivan'
            name   = 'Иванов Иван Иванович'
            repo   = 'https://github.com/ivanov-ivan/OOP'
        }
    }
}

checkPoints {
    checkPoint('КТ1') { date = '2024-11-01' }
}
```

### oop_checker.groovy (короткоживущий — основной файл запуска)
```groovy
include 'semester.groovy'

assignments {
    assign {
        students = ['ivanov-ivan', 'petrov-petr']
        tasks    = ['Task_1_1_1', 'Task_1_2_1']
    }
}

settings {
    testTimeout = 60
    gradeThresholds {
        excellent    = 85
        good         = 70
        satisfactory = 55
    }
    bonusPoints {
        student('ivanov-ivan') {
            task('Task_1_1_1') { bonus = 10 }
        }
    }
}
```

## Критерии оценивания

- **100%** баллов — сдано до мягкого дедлайна
- **50%** баллов  — сдано после мягкого, но до жёсткого дедлайна
- **0%** баллов   — сдано после жёсткого дедлайна
- Базовый балл пропорционален доле пройденных тестов
- Бонусы за Javadoc и соответствие Google Java Style (до 10%)
- Дополнительные баллы выставляются преподавателем в конфиге

## Сборка и запуск

```bash
# Сборка
./gradlew jar

# Запуск (ищет oop_checker.groovy в текущей директории)
java -jar build/libs/oop-checker-1.0-SNAPSHOT.jar > report.html

# Запуск с явным указанием конфига
java -jar build/libs/oop-checker-1.0-SNAPSHOT.jar path/to/oop_checker.groovy > report.html

# Тесты
./gradlew test
```

## Требования

- Java 17+
- Gradle 8+
- git (настроен без запроса пароля — SSH-ключи или credential helper)
- В репозиториях студентов ожидается Gradle или Maven как система сборки
- Задачи располагаются в поддиректориях репозитория с именем, совпадающим с ID задачи

## Что делает приложение

1. Читает `oop_checker.groovy` из рабочей директории (как Gradle читает `build.gradle`)
2. Для каждого студента из заданий: клонирует или обновляет (`git pull`) репозиторий
3. Для каждой задачи в репозитории:
   - Определяет дату последнего коммита (`git log`)
   - Запускает компиляцию (`gradle compileJava` / `mvn compile`)
   - Если OK: генерирует Javadoc и проверяет Google Java Style (Checkstyle)
   - Если OK: запускает тесты и парсит XML-отчёты JUnit
   - Считает балл с учётом дедлайна и результатов тестов
4. Суммирует баллы, вычисляет оценки на каждую КТ и итоговую
5. Выводит HTML-отчёт в stdout
