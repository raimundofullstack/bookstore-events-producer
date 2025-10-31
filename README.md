# 📚 Kafka Book Producer

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-Producer%2FConsumer-black?logo=apachekafka)
![License](https://img.shields.io/badge/license-MIT-blue)

Projeto em **Java Spring Boot** com foco em **Kafka Producer**, testes integrados e unitários. Este projeto demonstra o envio de eventos de livros para um broker Kafka local.
Este serviço complementa o [Kafka Book Consumer](https://github.com/raimundofullstack/bookstore-events-consumer)

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
