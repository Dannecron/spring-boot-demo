Метрики приложения в формате prometheus:

```shell
curl --request GET \
     --url 'http://localhost:8081/metrics'
```

Список доступных для отображения метрик приложения:
```shell
curl --request GET \
     --url 'http://localhost:8081/spring-metrics'
```

Значение конкретной метрики:
```shell
curl --request GET \
     --url 'http://localhost:8081/spring-metrics/<metric-name>' 
```
