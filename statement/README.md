## Микросервис "statement"
### Данный микросервис имеет 2 эндпоинта:

1. `POST` http://localhost:8082/statement

     Прескоринг + запрос на расчёт возможных условий кредита. Request - `LoanStatementRequestDto`, response - `List<LoanOfferDto>`

    - По API приходит `LoanStatementRequestDto`.
    - На основе `LoanStatementRequestDto` происходит прескоринг.
    - Отправляется POST-запрос на `/deal/statement` в МС deal через RestTemplate.
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

2. `POST` http://localhost:8082/statement/offer
  
   Выбор одного из предложений. Request - `LoanOfferDto`, response - `void`.

   - По API приходит `LoanOfferDto`.
   - Отправляется POST-запрос на `/deal/offer/select` в МС deal через RestTemplate.

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
