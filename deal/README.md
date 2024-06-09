## Микросервис "deal"
### Данный микросервис имеет 3 эндпоинта:

1. `POST` http://localhost:8081/deal/statement

     Расчёт возможных условий кредита. Request - `LoanStatementRequestDto`, response - `List<LoanOfferDto>`

    - Создается сущность `Client` и сохраняется в БД. 
    - Создаётся сущность `Statement` со связью на только что созданный `Client` и сохраняется в БД.
    - Отправляется POST запрос на `/calculator/offers` (МС Калькулятор) через RestTemplate для получения четырех предварительных кредитных предложений в виде списка `List<LoanOfferDto>`.
    - Каждому элементу из списка `List<LoanOfferDto>` присваивается id созданной заявки (`Statement`).
    - Ответ на API - список из 4х `LoanOfferDto` от "худшего" к "лучшему".

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

2. `POST` http://localhost:8081/deal/offer/select
  
   Выбор одного из предложений. Request - `LoanOfferDto`, response - `void`.

   - По API приходит `LoanOfferDto`.
   - Из БД достается заявка(`Statement`) по statementId из `LoanOfferDto`.
   - В заявке обновляется статус, история статусов(`List<StatementStatusHistoryDto>`), принятое предложение `LoanOfferDto` устанавливается в поле `appliedOffer`.
   - Заявка сохраняется.

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

3. `POST` http://localhost:8081/deal/calculate/{statementId}

   Завершение регистрации + полный подсчёт кредита. Request - `FinishRegistrationRequestDto`, param - `String`, response `void`.

   - По API приходит объект `FinishRegistrationRequestDto` и параметр `statementId` (String).
   - Достаётся из БД заявка(`Statement`) по `statementId`.
   - `ScoringDataDto` насыщается информацией из `FinishRegistrationRequestDto` и `Client`, который хранится в `Statement`.
   - Отправляется POST запрос на `/calculator/calc` (МС Калькулятор) с телом `ScoringDataDto` через RestTemplate.
   - На основе полученного из кредитного конвейера `CreditDto` создаётся сущность `Credit` и сохраняется в базу со статусом `CALCULATED`.
   - В заявке обновляется статус, история статусов.
   - Заявка сохраняется.

    *Пример:*

    http://localhost:8081/deal/calculate/aec3bee4-ba63-497e-ad20-e145cd7d9943

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
