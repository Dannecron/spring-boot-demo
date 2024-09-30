```shell
curl --request GET \
     --url 'http://localhost:8080/api/product/179cffdc-90f8-4627-985d-3d9c88dff5d7'
```

```shell
curl --request POST \
  --url http://localhost:8080/api/product \
  -H "Content-Type: application/json" \
  -d '{"name":"product-tree","description":"some other product","price":30000}'
```

```shell
curl --request DELETE \
     --url 'http://localhost:8080/api/product/179cffdc-90f8-4627-985d-3d9c88dff5d7'
```
