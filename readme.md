## Spring boot application

Demo приложение для изучения языка `kotlin` и фреймворка `spring boot`.

## Требования

* `jdk` версии `17`
* `postgresql` версии `14`
* `kafka` без авторизации
* `docker` + `compose`

## Доступные команды

* Запуск тестов
    ```shell
    ./gradlew test
    ```
  
* запуск приложения
    ```shell
    ./gradlew bootRun
    ```

## Запуск с docker-compose

Перед каждым запуском необходимо собрать приложение:
```shell
./gradlew assemble
```

Затем можно запускать контейнер:
```shell
docker compose up -d
```