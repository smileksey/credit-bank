## Микросервис "calculator"
### Данный микросервис имеет 2 эндпоинта:

1. `POST` http://localhost:8080/calculator/offers

   Получение четырех предварительных кредитных предложения на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true).

    Тело запроса:
   
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
        "statementId": "150671a0-61b4-484f-a518-58a00a0a901b",
        "requestedAmount": 500000,
        "totalAmount": 561566.28,
        "term": 12,
        "monthlyPayment": 46797.19,
        "rate": 22,
        "isInsuranceEnabled": false,
        "isSalaryClient": false
      },
      {
        "statementId": "b899056e-b5a0-4d95-a1a1-3b6a43a74101",
        "requestedAmount": 500000,
        "totalAmount": 558682.68,
        "term": 12,
        "monthlyPayment": 46556.89,
        "rate": 21,
        "isInsuranceEnabled": false,
        "isSalaryClient": true
      },
      {
        "statementId": "72710a80-f711-44ee-942e-0cd705be80dd",
        "requestedAmount": 550000,
        "totalAmount": 632587.92,
        "term": 12,
        "monthlyPayment": 52715.66,
        "rate": 18,
        "isInsuranceEnabled": true,
        "isSalaryClient": false
      },
      {
        "statementId": "78b2016f-83e7-4b17-a7fe-9e8ddcc68e66",
        "requestedAmount": 550000,
        "totalAmount": 601951.32,
        "term": 12,
        "monthlyPayment": 50162.61,
        "rate": 17,
        "isInsuranceEnabled": true,
        "isSalaryClient": true
      }
    ]
    ```

2. `POST` http://localhost:8080/calculator/calc
  
   Расчет итоговой ставки(rate), полной стоимости кредита(psk), размера ежемесячного платежа(monthlyPayment), графика ежемесячных платежей (List<PaymentScheduleElementDto>).

   Тело запроса:
   
   ```json
   {
      "amount": 500000,
      "term": 12,
      "firstName": "Ivan",
      "lastName": "Ivanov",
      "middleName": "Ivanovich",
      "gender": "MALE",
      "birthdate": "1990-01-10",
      "passportSeries": "1122",
      "passportNumber": "123456",
      "passportIssueDate": "2020-01-10",
      "passportIssueBranch": "УВД Советского р-на гор. Нижнего Новгорода",
      "maritalStatus": "MARRIED",
      "dependentAmount": 0,
      "employment": {
            "employmentStatus": "EMPLOYED",
            "employerINN": "1234567890",
            "salary": 50000,
            "position": "MIDDLE_MANAGER",
            "workExperienceTotal": 60,
            "workExperienceCurrent": 5
      },
      "accountNumber": "123456789",
      "isInsuranceEnabled": true,
      "isSalaryClient": false
    }
    ```

   Ответ `CreditDto`:

    ```json
    {
      "amount": 500000,
      "term": 12,
      "monthlyPayment": 46041.28,
      "rate": 10,
      "psk": 10.5,
      "isInsuranceEnabled": true,
      "isSalaryClient": false,
      "paymentSchedule": [
        {
          "number": 1,
          "date": "2024-06-01",
          "totalPayment": 46041.28,
          "interestPayment": 4098.36,
          "debtPayment": 41942.92,
          "remainingDebt": 458057.08
        },
        {
          "number": 2,
          "date": "2024-07-01",
          "totalPayment": 46041.28,
          "interestPayment": 3879.72,
          "debtPayment": 42161.56,
          "remainingDebt": 415895.52
        },
        {
          "number": 3,
          "date": "2024-08-01",
          "totalPayment": 46041.28,
          "interestPayment": 3522.61,
          "debtPayment": 42518.67,
          "remainingDebt": 373376.85
        },
        {
          "number": 4,
          "date": "2024-09-01",
          "totalPayment": 46041.28,
          "interestPayment": 3060.47,
          "debtPayment": 42980.81,
          "remainingDebt": 330396.04
        },
        {
          "number": 5,
          "date": "2024-10-01",
          "totalPayment": 46041.28,
          "interestPayment": 2798.44,
          "debtPayment": 43242.84,
          "remainingDebt": 287153.2
        },
        {
          "number": 6,
          "date": "2024-11-01",
          "totalPayment": 46041.28,
          "interestPayment": 2353.71,
          "debtPayment": 43687.57,
          "remainingDebt": 243465.63
        },
        {
          "number": 7,
          "date": "2024-12-01",
          "totalPayment": 46041.28,
          "interestPayment": 2062.14,
          "debtPayment": 43979.14,
          "remainingDebt": 199486.49
        },
        {
          "number": 8,
          "date": "2025-01-01",
          "totalPayment": 46041.28,
          "interestPayment": 1694.27,
          "debtPayment": 44347.01,
          "remainingDebt": 155139.48
        },
        {
          "number": 9,
          "date": "2025-02-01",
          "totalPayment": 46041.28,
          "interestPayment": 1190.11,
          "debtPayment": 44851.17,
          "remainingDebt": 110288.31
        },
        {
          "number": 10,
          "date": "2025-03-01",
          "totalPayment": 46041.28,
          "interestPayment": 936.7,
          "debtPayment": 45104.58,
          "remainingDebt": 65183.73
        },
        {
          "number": 11,
          "date": "2025-04-01",
          "totalPayment": 46041.28,
          "interestPayment": 535.76,
          "debtPayment": 45505.52,
          "remainingDebt": 19678.21
        },
        {
          "number": 12,
          "date": "2025-05-01",
          "totalPayment": 19845.34,
          "interestPayment": 167.13,
          "debtPayment": 19678.21,
          "remainingDebt": 0
        }
      ]
    }
    ```
