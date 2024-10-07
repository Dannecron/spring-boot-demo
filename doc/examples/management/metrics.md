### Requests

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

### Prometheus scrape

```yaml
scrape_configs:
  - job_name: spring-boot-demo
    metrics_path: /metrics
    static_configs:
      - targets: [spring-boot-demo-demo-1:8081]
```