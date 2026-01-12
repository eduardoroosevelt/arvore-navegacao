# arvore-navegacao

Backend Spring Boot 3 (Java 17) para retornar árvore de menus filtrada por permissões.

## Como rodar

```bash
mvn spring-boot:run
```

H2 console: http://localhost:8080/h2-console

## Credenciais seed

- admin / admin123 (ROLE_ADMIN)
- user / user123 (ROLE_USER)

## Exemplo de chamada

```bash
curl -u user:user123 http://localhost:8080/api/menu
```
