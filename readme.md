## Spring boot application

Demo приложение для изучения языка `kotlin` и фреймворка `spring boot`.

## Требования

* `jdk` версии `17`
* `postgresql` версии `14`
* `kafka` без авторизации
* `grafana-agent` (или аналогичный otlp-совместимый сборщик трейс-логов)
* `docker` + `compose`

## Доступные команды

* Запуск тестов
    ```shell
    ./gradlew test
    ```
  
    * после прохождения тестов можно увидеть [карту покрытия](/build/reports/tests/test/index.html).
  
* запуск приложения
    ```shell
    ./gradlew bootRun
    ```

## Запуск с docker-compose

Перед первым запуском необходимо:
* запустить отдельно контейнеры с postgres, kafka и grafana-agent (опционально)
* убедиться, что все запущенные контейнеры будут видеть контейнер с приложением (например, добавить везде сеть `spring-boot-demo_default`)
* скопировать [.env.example](/.env.example) в [.env](/.env) и изменить конфигурацию

После каждого изменения в исходный код необходимо собрать приложение:
```shell
./gradlew assemble
```

Затем можно запускать контейнер:
```shell
docker compose up --build
```
