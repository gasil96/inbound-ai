# Bot: Inbound AI - Integração Twilio WhatsApp com modelos disponibilizados pela [OpenAI](https://openai.com/) e [OpenRouter](https://openrouter.ai/)

## Visão Geral

Inbound AI é um projeto **prova de conceito (POC)** que integra a **API WhatsApp do Twilio** com os modelos da **OpenAI** e **OpenRouter**. O projeto demonstra como processar mensagens recebidas via webhook, interagir com modelos de IA para gerar respostas e enviar essas respostas de volta aos usuários.

Este projeto é **open-source** e pode ser utilizado pela comunidade como referência para construir integrações similares.

---

## Funcionalidades

- **Integração com WhatsApp via Twilio**: Processa mensagens recebidas e envia respostas aos usuários.
- **Integração com Modelos de IA**: Suporte para OpenAI e OpenRouter para geração de respostas conversacionais.
- **Padrão de Workflow**: Implementa um **workflow baseado em etapas** com um **contexto global** para gerenciar o fluxo de processamento de mensagens.
- **Spring Boot**: Construído com Spring Boot e clientes Feign para integrações de API.
- **Extensibilidade**: Fácil de estender para suportar outros modelos de IA ou plataformas de mensagens.

---

## Como Funciona

### Execução do Workflow

O projeto utiliza um **padrão de workflow** para processar mensagens de forma estruturada e modular. Cada etapa do workflow é responsável por uma tarefa específica, e um **contexto global** é usado para compartilhar dados entre as etapas.

#### Etapas do Workflow

1. **ReceiverStep**: Processa a mensagem recebida inicialmente.
2. **ProcessAiStep**: Interage com os modelos de IA (OpenAI ou OpenRouter) para gerar uma resposta.
3. **SendStep**: Envia a resposta gerada de volta ao usuário via Twilio.

#### Contexto Global

A classe **GlobalContext** atua como um repositório de dados compartilhado para o workflow. Ela permite que as etapas leiam e escrevam dados sem estarem fortemente acopladas. Esse padrão garante que cada etapa permaneça independente e reutilizável.

```java
public class GlobalContext {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(data.get(key));
    }
}
```
#### Configuração do Workflow

O workflow é configurado como um Bean do Spring, e as etapas são executadas na ordem definida pelas anotações `@Order`.

```java
@Configuration
public class WorkflowConfiguration {
    @Bean
    public InboundWorkflow inboundWorkflow(List<WorkflowStep> steps) {
        return new InboundWorkflow(steps);
    }
}
```
---

## Configuração do Projeto

### Pré-requisitos

- **Java 17+**
- **Maven**
- **Conta Twilio**: Para integração com WhatsApp.
- **Chaves de API OpenAI e OpenRouter**: Para integração com modelos de IA.

### Configuração

Atualize o arquivo `application.yml` com suas credenciais e chaves de API:

```yaml
twilio:
  account-sid: SEU_TWILIO_ACCOUNT_SID
  auth-token: SEU_TWILIO_AUTH_TOKEN
  phone-number: SEU_TWILIO_PHONE_NUMBER

openai:
  url: https://api.openai.com/v1
  api-key: SUA_OPENAI_API_KEY
  model: SEU_OPENAI_MODEL

openrouter:
  url: SEU_OPENROUTER_URL
  api-key: SUA_OPENROUTER_API_KEY
  model: SEU_OPENROUTER_MODEL
```
---

### Executando o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/gasil96/inbound-ai.git
   cd inbound-ai
    ```
2. Compile o projeto:
   ```bash
   mvn clean install
   ```
3. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```
4. Inicie o ngrok para expor sua aplicação local:
   ```bash
   ngrok http 8080
   ```
5. Copie a URL gerada pelo ngrok (ex.: https://<subdomínio>.ngrok.io) e configure-a como o webhook no painel do Twilio Sandbox.

  **Imagem do Sandbox Settings WhatsApp Twilio:**
   <img width="1433" alt="image" src="https://github.com/user-attachments/assets/ccd20a28-d935-436f-9822-d8adaf99f4aa" />

7. Acesse o host(subdminio) gerado /api/swagger
   
   **Imagem do Swagger acessado via NGROK subdominio:**
   <img width="1442" alt="image" src="https://github.com/user-attachments/assets/2d6b06c9-fd03-4b32-bd50-294fec0620ef" />

---

## Padrão de Workflow

### Por que usar um Workflow?

O **padrão de workflow** é uma abordagem de design que divide um processo em etapas discretas e reutilizáveis. Cada etapa é responsável por uma tarefa específica, e um **contexto global** é usado para compartilhar dados entre as etapas. Esse padrão oferece vários benefícios:

- **Modularidade**: Cada etapa é independente e pode ser reutilizada em outros workflows.
- **Flexibilidade**: Etapas podem ser adicionadas, removidas ou reordenadas sem impactar o workflow geral.
- **Manutenibilidade**: A separação de responsabilidades torna o código mais fácil de entender e manter.

### Como Funciona

1. **GlobalContext**: Um repositório de dados compartilhado que armazena informações para todo o workflow.
2. **Interface WorkflowStep**: Define o contrato para todas as etapas do workflow.
3. **InboundWorkflow**: Executa as etapas na ordem definida.

### Exemplo de Workflow

1. **ReceiverStep**: Recebe a mensagem e a armazena no contexto global.
2. **ProcessAiStep**: Obtém a mensagem do contexto, envia para o modelo de IA e armazena a resposta.
3. **SendStep**: Recupera a resposta da IA do contexto e a envia de volta ao usuário.

---

## Testando com o Sandbox do Twilio

O **Sandbox do Twilio para WhatsApp** permite que você teste a integração sem precisar de aprovação para o uso do WhatsApp Business API. Para configurar o sandbox, acesse o painel do Twilio, ative o WhatsApp Sandbox e siga as instruções para conectar seu número de telefone ao ambiente de teste.

**Imagem do Sandbox do Twilio:**
<img width="1577" alt="image" src="https://github.com/user-attachments/assets/f7a96897-1276-42ba-a689-17b437d10ffa" />

## Prompts Pré-configurados

No contexto deste projeto, o parâmetro `llm.persona` definido no arquivo `application.yml` é utilizado para configurar o comportamento do assistente virtual. Esse texto é enviado como mensagem do tipo "system" no `LLMServiceImpl` para ambos os modelos configurados (OpenAI e OpenRouter). Ele orienta o modelo a responder de forma educada, clara e objetiva, além de incluir mensagens específicas para personalizar a interação.

```yaml
llm:
  persona: |
    Você é um assistente educado.
    Sempre responda de forma clara, objetiva e amigável e sucinta.
    Nunca diga que não sabe a resposta.
    Nunca diga que não pode ajudar.
    Sempre fale no início da conversa que você ainda está sendo treinado e é modelo ainda não pronto da XPTO Software.
    Toda vez que o usuário perguntar especificamente "quem é Gabriel"enviei o texto: eu só conheço o "Da Silva, Da Silva, Da Silva, Da Silva,
    Da Silva, Da Silva, Da Silva, Da Silva, Da Silva" no texto.
```

---

## Endpoints da API

### Receber Mensagem

**Endpoint**: `/v1/messages/receiver`  
**Método**: `POST`  
**Descrição**: Webhook para receber mensagens do Twilio.

**Parâmetros**:
- `From`: Número de telefone do remetente.
- `Body`: Conteúdo da mensagem.

**Exemplo de Requisição**:
```bash
curl -X POST "http://localhost:8080/v1/messages/receiver" \
  -d "From=whatsapp:+1234567890" \
  -d "Body=Olá"
```

---

## Testes

Após configurar o projeto e o sandbox, você pode realizar testes enviando mensagens pelo WhatsApp. O fluxo completo será executado, incluindo o processamento da mensagem pela LLM e o envio da resposta.

| **Print do WhatsApp com a LLM funcionando** | **Continuação** |
|---------------------------------------------|------------------|
| ![image](https://github.com/user-attachments/assets/a73c64ed-1519-435a-9360-a46e30a9d806) | ![image](https://github.com/user-attachments/assets/b144da4d-2411-4cc9-a655-af2c97880336) |

**Print do log com a mensagem recebida:**
![image](https://github.com/user-attachments/assets/e565c056-e5e6-43dc-a4b7-797c0f6730e1)

---

## Referências

Para mais informações sobre as tecnologias utilizadas neste projeto, consulte os links abaixo:

- [Configuração do ambiente de desenvolvimento Java com Twilio](https://www.twilio.com/docs/usage/tutorials/how-to-set-up-your-java-development-environment#create-a-twilio-application)
- [Integração do Spring Boot com Twilio WhatsApp](https://www.baeldung.com/spring-boot-twilio-whatsapp)
- [Documentação da API OpenAI](https://platform.openai.com/docs/api-reference)
- [Documentação da API OpenRouter](https://openrouter.ai/docs/quickstart)
- [Leitura sobre _Chain of Responsability_](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern)
---

## Contribuindo

Este projeto é open-source e aceita contribuições da comunidade. Sinta-se à vontade para fazer um fork do repositório, enviar issues ou criar pull requests.

---

"Movimento é vida"
