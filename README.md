# 📚 Kafka BookStore Producer

![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=flat-square&logo=spring)
![Spring Kafka](https://img.shields.io/badge/Spring%20Kafka-3.6+-6DB33F?style=flat-square&logo=apachekafka)
![Lombok](https://img.shields.io/badge/Lombok-1.18.26-FC2F2F?style=flat-square&logo=lombok)
![Spring Web](https://img.shields.io/badge/Spring%20Web-3.5.7-6DB33F?style=flat-square&logo=spring)
![Spring Validation](https://img.shields.io/badge/Spring%20Validation-3.5.7-6DB33F?style=flat-square&logo=spring)
![JUnit 5](https://img.shields.io/badge/JUnit_5-5.10.0-25A162?style=flat-square&logo=junit5)
![Kafka Test](https://img.shields.io/badge/Kafka_Test-3.6+-231F20?style=flat-square&logo=apachekafka)
![License](https://img.shields.io/badge/license-MIT-blue)
## 🚀 Tecnologias e Dependências



Projeto em **Java Spring Boot** com foco em **Kafka Producer**, testes integrados e unitários. Este projeto demonstra o envio de eventos de livros para um broker Kafka local.
Este serviço complementa o [Kafka BookStore Consumer](https://github.com/raimundofullstack/bookstore-events-consumer)

---

## 🧩Descrição

O projeto contém endpoints REST para criar e atualizar livros, enviando eventos correspondentes para o Kafka. Ideal para estudos de microservices, mensageria e integração com Kafka.

- **POST /books** – Cria um novo livro e envia evento para Kafka.
- **PUT /books/{id}** – Atualiza informações de um livro existente e envia evento de atualização.
- Kafka configurado localmente para fins de desenvolvimento e testes.

## 🚀 Tecnologias e Dependências

- ☕ Java 17
- 🌱 Spring Boot 3.5.7
- 🔄 Spring Kafka 3.6+
- 🧩 Lombok
- 🌐 Spring Web
- ✅ Spring Validation
- 🧪 JUnit 5 (testes unitários e integrados)
- 🛠️ Kafka Test (testes específicos para Kafka)

## 🧱 Estrutura do Projeto
```text
src/main/java/com/martins
├── config/ # Configuração de topicos
├── controller/ # Endpoints REST(por não ser o foco, não criei o service)
├── domain/ # Data Transfer Objects
└── producer/ # Realiza o envio dos eventos streams a um tópico no kafka
```

Exemplo do `build.gradle`:

```gradle
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```
## 🧪 Testes
O projeto possui testes unitários e integrados para:
* Validação de dados
* Produção de eventos no Kafka
* Comportamento dos endpoints

```gradle
./gradlew test
```

👨‍💻 Autor

Raimundo Martins | Desenvolvedor Full Stack
