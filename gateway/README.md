## Микросервис "gateway"
### Входная точка в приложение. Реализует микросервисный паттерн API-Gateway для сервисов кредитного конвейера. Клиент отправляет все запросы в gateway, который перенаправляет их в другие микросервисы и возвращает их ответ.
### Данный микросервис имеет 6 эндпоинтов:

1. `POST` http://localhost:8080/statement

     Прескоринг + запрос на расчёт возможных условий кредита. Request - `LoanStatementRequestDto`, response - `List<LoanOfferDto>`

    - По API приходит `LoanStatementRequestDto`.
    - Запрос перенаправляется в МС 'statement' на `/statement` через RestTemplate.
    - Возвращается ответ от МС 'statement' - список из 4х `LoanOfferDto` от "худшего" к "лучшему".

    *Пример:*
   
    Тело запроса `LoanStatementRequestDto`:
   
   ```json
   {
      "amount": 500000,
      "term": 12,
      "firstName": "Ivan",
      "lastName": "Ivanov",
      "middleName": "Ivanovich",
      "email": "ivanov@mail.ru",
      "birthdate": "1990-01-10",
      "passportSeries": "1122",
      "passportNumber": "123456"
    }
    ```

    Ответ `List<LoanOfferDto>`:

    ```json
    [
      {
        "statementId": "aec3bee4-ba63-497e-ad20-e145cd7d9943",
        "requestedAmount": 500000.00,
        "totalAmount": 561566.28,
        "term": 12,
        "monthlyPayment": 46797.19,
        "rate": 22.00,
        "isInsuranceEnabled": false,
        "isSalaryClient": false
      },
      {
        "statementId": "aec3bee4-ba63-497e-ad20-e145cd7d9943",
        "requestedAmount": 500000.00,
        "totalAmount": 558682.68,
        "term": 12,
        "monthlyPayment": 46556.89,
        "rate": 21.00,
        "isInsuranceEnabled": false,
        "isSalaryClient": true
      },
      {
        "statementId": "aec3bee4-ba63-497e-ad20-e145cd7d9943",
        "requestedAmount": 550000.00,
        "totalAmount": 632587.92,
        "term": 12,
        "monthlyPayment": 52715.66,
        "rate": 18.00,
        "isInsuranceEnabled": true,
        "isSalaryClient": false
      },
      {
        "statementId": "aec3bee4-ba63-497e-ad20-e145cd7d9943",
        "requestedAmount": 550000.00,
        "totalAmount": 601951.32,
        "term": 12,
        "monthlyPayment": 50162.61,
        "rate": 17.00,
        "isInsuranceEnabled": true,
        "isSalaryClient": true
      }
    ]
    ```
---

2. `POST` http://localhost:8080/statement/select
  
   Выбор одного из кредитных предложений. Request - `LoanOfferDto`, response - `void`.

   - По API приходит `LoanOfferDto`.
   - Запрос перенаправляется в МС 'statement' на `/statement/offer` через RestTemplate.

   *Пример:*

   Тело запроса `LoanOfferDto`:
   
   ```json
   {
     "statementId": "aec3bee4-ba63-497e-ad20-e145cd7d9943",
     "requestedAmount": 500000.00,
     "totalAmount": 561566.28,
     "term": 12,
     "monthlyPayment": 46797.19,
     "rate": 22.00,
     "isInsuranceEnabled": false,
     "isSalaryClient": false
    }
    ```
---

3. `POST` http://localhost:8080/statement/registration/{statementId}

   Завершение регистрации + полный подсчёт кредита. Request - `FinishRegistrationRequestDto`, param - `String`, response - `void`.

   - По API приходит объект `FinishRegistrationRequestDto` и параметр `statementId` (String).
   - Запрос перенаправляется в МС 'deal' на `/deal/calculate/{statementId}` через RestTemplate.

    *Пример:*

    http://localhost:8080/statement/registration/aec3bee4-ba63-497e-ad20-e145cd7d9943


    Тело запроса `FinishRegistrationRequestDto`:

   ```json
   {
     "gender": "MALE",
     "maritalStatus": "MARRIED",
     "dependentAmount": 0,
     "passportIssueDate": "2020-05-01",
     "passportIssueBranch": "УВД Советского р-на гор. Нижнего Новгорода",
     "employment": {
                    "employmentStatus": "EMPLOYED",
                    "employerINN": "1234567890",
                    "salary": 80000.00,
                    "position": "MID_MANAGER",
                    "workExperienceTotal": 60,
                    "workExperienceCurrent": 3
                    },
     "accountNumber": "123456789"
   }
    ```
---

4. `POST` http://localhost:8080/document/{statementId}

   Запрос на отправку документов. Request - `void`, param - `String`, response - `void`.

   - По API приходит запрос с параметром `statementId`.
   - Запрос перенаправляется в МС 'deal' на `/deal/document/{statementId}/send` через RestTemplate.

---

5. `POST` http://localhost:8080/document/{statementId}/sign

   Запрос на подписание документов. Request - `void`, param - `String`, response - `void`.

   - По API приходит запрос с параметром `statementId`.
   - Запрос перенаправляется в МС 'deal' на `/deal/document/{statementId}/sign` через RestTemplate.
---

6. `POST` http://localhost:8080/document/{statementId}/sign/code

   Подписание документов с помощью кода ПЭП. Request - `SESCodeDto`, param - `String`, response - `void`.

   - По API приходит объект `SESCodeDto` и параметр `statementId`.
   - Запрос перенаправляется в МС 'deal' на `/deal/document/{statementId}/code` через RestTemplate.

   *Пример:*

    http://localhost:8080/document/aec3bee4-ba63-497e-ad20-e145cd7d9943/sign/code

    Тело запроса `SESCodeDto`:

   ```json
   {
    "sesCode": "3971e197-3bb6-4a9a-9266-ab396b8b1693"
   }
    ```
