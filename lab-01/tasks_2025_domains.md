# Доменні профілі

## Спільні правила (однакові для всіх доменів)

**Класи.** Усі сутності — звичайні класи (жодних `record`) і всі успадковують `BaseEntity` з пакета `ua.common`: їхні конструктори починаються з `super()`. Поля — `private`, геттери — `public`. Успадковане поле `createdAt` в опис сутностей нижче не входить і в `equals`/`hashCode` не бере участі.

**✎** біля поля означає: поле змінюється протягом життя обʼєкта → до нього є `public final` сеттер із такою самою валідацією, як у конструкторі. Поля без ✎ задаються тільки в конструкторі, сеттера не мають.

**🔑** біля поля: за ним сутність ідентифікується → саме воно йде в `equals`/`hashCode`. У конструкторі перевіряється як звичайне поле; те, що двох обʼєктів з однаковою identity не існує, у цій лабі не перевіряється взагалі (це задача репозиторію, буде далі).

**🔑❓** У кожному варіанті рівно одна сутність не має готового ключа — набір полів для `equals` визначаєте самі й обґрунтовуєте на захисті: просте поле? комбінація? Питання: *що означає, що два такі обʼєкти рівні?*

**Поля-переліки** (`status`, `type`, `category`…) — це `String`. Допустимі значення перелічені в профілі; тримайте їх у константах поруч із перевіркою. Значення нормалізується до UPPERCASE і перевіряється на належність до списку; інакше — `IllegalArgumentException`, у повідомленні перелічіть допустимі.

**Дати** — `LocalDate`, час — `LocalTime`, момент — `LocalDateTime`.

**Гроші** — `double` як свідоме навчальне спрощення; чому в реальних системах гроші не `double` — питання захисту.

**Помилки валідації** — `IllegalArgumentException` з інформативним повідомленням: що очікувалось і що отримано.

**Обчислювані методи.** У кожному варіанті наприкінці вказані два — вони живуть у `<Домен>Utils`, а не в полях сутності.

---

## Карта варіантів

| # | Домен | Сутність з 🔑❓ |
|---|---|---|
| [1](#варіант-1-готельне-бронювання) | Готельне бронювання | `Booking` |
| [2](#варіант-2-бронювання-столиків-у-ресторані) | Бронювання столиків | `Reservation` |
| [3](#варіант-3-бібліотечна-система) | Бібліотечна система | `BookLoan` |
| [4](#варіант-4-фітнес-клуб) | Фітнес-клуб | `WorkoutSession` |
| [5](#варіант-5-продаж-авіаквитків) | Продаж авіаквитків | `FlightTicket` |
| [6](#варіант-6-медична-клініка) | Медична клініка | `Appointment` |
| [7](#варіант-7-інтернет-магазин) | Інтернет-магазин | `OrderItem` |
| [8](#варіант-8-оренда-нерухомості) | Оренда нерухомості | `RentPayment` |
| [9](#варіант-9-управління-конференціями) | Управління конференціями | `EventRegistration` |
| [10](#варіант-10-логістика-та-вантажоперевезення) | Логістика | `Waybill` |
| [11](#варіант-11-салон-краси-та-спа) | Салон краси та СПА | `ClientAppointment` |
| [12](#варіант-12-банківське-кредитування) | Банківське кредитування | `RepaymentScheduleItem` |
| [13](#варіант-13-автосервіс-та-сто) | Автосервіс та СТО | `ServiceOrder` |
| [14](#варіант-14-служба-підтримки-it) | Служба підтримки IT | `WorkLog` |
| [15](#варіант-15-прокат-спортивного-спорядження) | Прокат спорядження | `RentalAgreement` |
| [16](#варіант-16-онлайн-курси-та-навчання) | Онлайн-курси | `CourseEnrollment` |
| [17](#варіант-17-автопарковка) | Автопарковка | `ParkingTicket` |
| [18](#варіант-18-притулок-для-тварин) | Притулок для тварин | `CareSchedule` |
| [19](#варіант-19-оренда-автомобілів) | Оренда автомобілів | `Rental` |
| [20](#варіант-20-музичний-стримінговий-каталог) | Музичний стримінг | `PlaylistTrack` |

---

## Варіант 1. Готельне бронювання

**Hotel Booking** — облік кімнат, гостей, бронювань та оплати проживання.

**`Guest`**
- `String passportNumber` 🔑 — непорожній, пробіли обрізаються
- `String firstName`, `String lastName` — непорожні
- `LocalDate birthDate` — не null; гостю щонайменше 18 років

**`Room`**
- `int roomNumber` 🔑 — строго > 0
- `String roomType` — `SINGLE`, `DOUBLE`, `SUITE`, `DELUXE`
- `double pricePerNight` ✎ — строго > 0

**`Booking`** — 🔑❓
- `Room room` — не null
- `Guest guest` — не null
- `LocalDate checkInDate` — не null
- `LocalDate checkOutDate` — не null; строго пізніша за `checkInDate`
- `String status` ✎ — `CONFIRMED`, `CHECKED_IN`, `CHECKED_OUT`, `CANCELLED`

**`HotelPayment`**
- `String paymentId` 🔑 — код транзакції (напр. `"PAY-8801"`); непорожній
- `Booking booking` — не null
- `double amount` — строго > 0
- `LocalDate paymentDate` — не null; не раніша за `booking.getCheckInDate()`

> **Обчислювані:** `nights(Booking)` — кількість ночей · `totalPrice(Booking)` — `nights × room.getPricePerNight()`

---

## Варіант 2. Бронювання столиків у ресторані

**Restaurant Reservation** — посадки, столики, резервації та замовлення.

**`Customer`**
- `String phone` 🔑 — непорожній
- `String name` — непорожнє
- `String email` — непорожній, містить `@`

**`RestaurantTable`**
- `int tableNumber` 🔑 — від 1 до 200
- `int capacity` — від 1 до 20
- `String zone` — `MAIN_HALL`, `TERRACE`, `VIP`, `BAR`

**`Reservation`** — 🔑❓
- `Customer customer` — не null
- `RestaurantTable table` — не null
- `LocalDate reservationDate` — не null
- `LocalTime startTime` — не null
- `LocalTime endTime` — не null; строго пізніший за `startTime`
- `int guestCount` — від 1 до `table.getCapacity()`
- `String status` ✎ — `PENDING`, `CONFIRMED`, `SEATED`, `COMPLETED`, `CANCELLED`

**`RestaurantOrder`**
- `String orderId` 🔑 — напр. `"ORD-501"`; непорожній
- `Reservation reservation` — не null
- `double totalAmount` — >= 0
- `double discountAmount` ✎ — від 0 до `totalAmount`

> **Обчислювані:** `durationMinutes(Reservation)` · `finalAmount(RestaurantOrder)` — сума мінус знижка

---

## Варіант 3. Бібліотечна система

**Library Management** — книжковий фонд, автори, читачі, видача книжок.

**`Reader`**
- `String readerTicketNumber` 🔑 — непорожній
- `String fullName` — непорожнє
- `LocalDate birthDate` — не null; читачу щонайменше 14 років

**`Author`**
- `String authorCode` 🔑 — непорожній
- `String name` — непорожнє
- `int birthYear` — від 1800 до поточного року

**`Book`**
- `String isbn` 🔑 — непорожній
- `String title` — непорожня
- `Author author` — не null
- `String genre` — `FICTION`, `SCIENCE`, `HISTORY`, `BIOGRAPHY`
- `int publishedYear` — від `author.getBirthYear()` до поточного року

**`BookLoan`** — 🔑❓
- `Book book` — не null
- `Reader reader` — не null
- `LocalDate issueDate` — не null; не в майбутньому
- `LocalDate dueDate` — не null; строго пізніша за `issueDate`
- `LocalDate returnDate` ✎ — може бути null (книгу ще не повернули); якщо вказано — `>= issueDate`
- `String status` ✎ — `ACTIVE`, `RETURNED`, `OVERDUE`

> **Обчислювані:** `loanDays(BookLoan)` — `dueDate − issueDate` · `overdueDays(BookLoan)` — наскільки повернули пізніше строку (0, якщо вчасно або ще не повернули)

---

## Варіант 4. Фітнес-клуб

**Gym & Fitness Club** — члени клубу, тренери, абонементи, персональні тренування.

**`Member`**
- `String taxId` 🔑 — ІПН або номер паспорта; непорожній
- `String fullName` — непорожнє
- `LocalDate birthDate` — не null; щонайменше 16 років

**`Trainer`**
- `String trainerId` 🔑 — табельний номер; непорожній
- `String name` — непорожнє
- `String specialty` — `YOGA`, `CROSSFIT`, `PILATES`, `GYM`

**`MembershipPass`**
- `String passCode` 🔑 — непорожній
- `Member member` — не null
- `String type` — `STANDARD`, `PREMIUM`, `VIP`, `STUDENT`
- `LocalDate startDate` — не null
- `LocalDate endDate` — не null; строго пізніша за `startDate`
- `double price` — строго > 0

**`WorkoutSession`** — 🔑❓
- `Trainer trainer` — не null
- `MembershipPass pass` — не null
- `LocalDate sessionDate` — не null; у межах `pass.getStartDate()` .. `pass.getEndDate()`
- `int durationMinutes` — від 30 до 180

> **Обчислювані:** `passDurationDays(MembershipPass)` · `pricePerDay(MembershipPass)` — вартість абонемента за добу

---

## Варіант 5. Продаж авіаквитків

**Airline Ticketing** — пасажири, аеропорти, рейси, продані квитки.

**`Passenger`**
- `String passportSeriesNum` 🔑 — непорожній
- `String fullName` — непорожнє
- `LocalDate birthDate` — не null; не в майбутньому

**`Airport`**
- `String iataCode` 🔑 — 3 латинські літери (напр. `"KBP"`); нормалізується до UPPERCASE
- `String cityName` — непорожнє

**`Flight`**
- `String flightNumber` 🔑 — напр. `"PS-101"`; непорожній
- `Airport departureAirport` — не null
- `Airport arrivalAirport` — не null; не збігається з `departureAirport`
- `LocalDateTime departureTime` — не null
- `LocalDateTime arrivalTime` — не null; строго пізніший за `departureTime`
- `String status` ✎ — `SCHEDULED`, `BOARDING`, `DELAYED`, `COMPLETED`, `CANCELLED`

**`FlightTicket`** — 🔑❓
- `Flight flight` — не null
- `Passenger passenger` — не null
- `String seatClass` — `ECONOMY`, `BUSINESS`, `FIRST`
- `double price` — строго > 0

> **Обчислювані:** `flightDurationMinutes(Flight)` · `route(Flight)` — рядок виду `"KBP → WAW"`

---

## Варіант 6. Медична клініка

**Medical Clinic** — запис пацієнтів до лікарів, медичні висновки, оплати.

**`Patient`**
- `String nationalId` 🔑 — номер мед. картки або паспорта; непорожній
- `String fullName` — непорожнє
- `LocalDate birthDate` — не null; не в майбутньому

**`Doctor`**
- `String licenseNumber` 🔑 — непорожній
- `String name` — непорожнє
- `String specialization` — `CARDIOLOGY`, `NEUROLOGY`, `PEDIATRICS`, `DENTISTRY`

**`Appointment`** — 🔑❓
- `Patient patient` — не null
- `Doctor doctor` — не null
- `LocalDate appointmentDate` — не null
- `LocalTime startTime` — не null
- `LocalTime endTime` — не null; строго пізніший за `startTime`
- `String status` ✎ — `SCHEDULED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`

**`MedicalRecord`**
- `String recordId` 🔑 — напр. `"REC-9001"`; непорожній
- `Appointment appointment` — не null
- `String diagnosis` — непорожній
- `double treatmentCost` — >= 0

> **Обчислювані:** `durationMinutes(Appointment)` · `patientAge(Patient)` — повних років

---

## Варіант 7. Інтернет-магазин

**Online Store** — каталог товарів, клієнти, замовлення, товарні позиції.

**`Customer`**
- `String email` 🔑 — непорожня, нормалізується до нижнього регістру
- `String name` — непорожнє
- `String phone` — непорожній

**`Product`**
- `String sku` 🔑 — артикул (напр. `"PROD-1092"`); непорожній
- `String title` — непорожня
- `double price` ✎ — строго > 0

**`Order`**
- `String orderId` 🔑 — непорожній
- `Customer customer` — не null
- `LocalDate orderDate` — не null; не в майбутньому
- `String status` ✎ — `NEW`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELLED`
- `double totalAmount` ✎ — >= 0

**`OrderItem`** — 🔑❓
- `Order order` — не null
- `Product product` — не null
- `int quantity` ✎ — від 1 до 1000
- `double unitPrice` — строго > 0; це **знімок** ціни на момент замовлення, з `product.getPrice()` **не** звіряється

> **Обчислювані:** `lineTotal(OrderItem)` — `quantity × unitPrice` · `priceDifference(OrderItem)` — наскільки ціна в замовленні відрізняється від поточної ціни товару

---

## Варіант 8. Оренда нерухомості

**Real Estate Rental** — обʼєкти, орендодавці, договори оренди, щомісячні платежі.

**`Landlord`**
- `String taxCode` 🔑 — ІПН орендодавця; непорожній
- `String fullName` — непорожнє
- `String phone` — непорожній

**`Property`**
- `String cadastralNumber` 🔑 — непорожній
- `String address` — непорожня
- `Landlord landlord` — не null
- `String type` — `APARTMENT`, `HOUSE`, `COMMERCIAL`, `GARAGE`
- `double monthlyRent` ✎ — строго > 0

**`LeaseContract`**
- `String contractNumber` 🔑 — напр. `"CNT-2026-01"`; непорожній
- `Property property` — не null
- `String tenantName` — непорожнє
- `LocalDate startDate` — не null
- `LocalDate endDate` — не null; строго пізніша за `startDate`
- `double depositAmount` — `>= property.getMonthlyRent()`
- `String status` ✎ — `ACTIVE`, `TERMINATED`, `EXPIRED`

**`RentPayment`** — 🔑❓
- `LeaseContract contract` — не null
- `double amount` — строго > 0
- `LocalDate paymentDate` — не null; не раніша за `contract.getStartDate()`

> **Обчислювані:** `contractMonths(LeaseContract)` — тривалість договору в місяцях · `totalRent(LeaseContract)` — місяці × місячна плата

---

## Варіант 9. Управління конференціями

**Event & Conference Management** — конференції, локації, учасники, реєстрації.

**`Participant`**
- `String email` 🔑 — непорожня, нижній регістр
- `String fullName` — непорожнє
- `String organization` — непорожня

**`Venue`**
- `String venueCode` 🔑 — напр. `"HALL-A"`; непорожній
- `String name` — непорожня
- `int capacity` — строго > 0

**`Event`**
- `String eventCode` 🔑 — непорожній
- `String title` — непорожня
- `Venue venue` — не null
- `String category` — `TECH`, `SCIENCE`, `BUSINESS`, `ART`
- `LocalDate startDate` — не null
- `LocalDate endDate` — не null; `>= startDate`

**`EventRegistration`** — 🔑❓
- `Event event` — не null
- `Participant participant` — не null
- `String ticketType` — `STANDARD`, `VIP`, `SPEAKER`, `PRESS`
- `LocalDate registrationDate` — не null; `<= event.getStartDate()`

> **Обчислювані:** `eventDays(Event)` — скільки днів триває подія · `daysBeforeEvent(EventRegistration)` — за скільки днів до початку зареєструвались

---

## Варіант 10. Логістика та вантажоперевезення

**Cargo Delivery** — вантажі, клієнти, автопарк, товарно-транспортні накладні.

**`Client`**
- `String taxId` 🔑 — ЄДРПОУ / ІПН; непорожній
- `String companyName` — непорожня
- `String contactPhone` — непорожній

**`Vehicle`**
- `String vinCode` 🔑 — непорожній
- `String model` — непорожня
- `double maxWeightKg` — вантажопідйомність; строго > 0

**`Shipment`**
- `String trackingNumber` 🔑 — непорожній
- `Client sender` — не null
- `String cargoType` — `STANDARD`, `FRAGILE`, `HAZARDOUS`, `PERISHABLE`
- `double cargoWeightKg` — строго > 0
- `LocalDate creationDate` — не null; не в майбутньому
- `String status` ✎ — `REGISTERED`, `IN_TRANSIT`, `DELIVERED`, `RETURNED`

**`Waybill`** — 🔑❓
- `Shipment shipment` — не null
- `Vehicle vehicle` — не null; `shipment.getCargoWeightKg() <= vehicle.getMaxWeightKg()`
- `LocalDate dispatchDate` — не null; `>= shipment.getCreationDate()`
- `LocalDate estimatedArrival` — не null; `>= dispatchDate`

> **Обчислювані:** `transitDays(Waybill)` · `loadPercent(Waybill)` — на скільки відсотків завантажена машина

---

## Варіант 11. Салон краси та СПА

**Beauty Salon & Spa** — запис клієнтів до майстрів, каталог послуг, візити.

**`BeautyClient`**
- `String phone` 🔑 — непорожній
- `String name` — непорожнє
- `LocalDate birthDate` — не null; щонайменше 14 років

**`Master`**
- `String employeeId` 🔑 — непорожній
- `String fullName` — непорожнє
- `String category` — `HAIRCUT`, `MANICURE`, `MASSAGE`, `FACIAL`

**`BeautyService`**
- `String serviceCode` 🔑 — непорожній
- `String name` — непорожня
- `String category` — той самий список, що в `Master`
- `int durationMinutes` — від 15 до 240
- `double price` ✎ — строго > 0

**`ClientAppointment`** — 🔑❓
- `BeautyClient client` — не null
- `Master master` — не null
- `BeautyService service` — не null; `master.getCategory()` збігається з `service.getCategory()`
- `LocalDate appointmentDate` — не null
- `LocalTime startTime` — не null
- `String state` ✎ — `CONFIRMED`, `COMPLETED`, `CANCELLED`, `NO_SHOW`

> **Обчислювані:** `endTime(ClientAppointment)` — `startTime + service.getDurationMinutes()`, полем **не** зберігається · `pricePerMinute(BeautyService)`

---

## Варіант 12. Банківське кредитування

**Bank Loan System** — позичальники, кредити, графіки погашення.

**`Borrower`**
- `String passportNum` 🔑 — непорожній
- `String fullName` — непорожнє
- `LocalDate birthDate` — не null; щонайменше 21 рік
- `double monthlyIncome` ✎ — строго > 0

**`BankBranch`**
- `String branchCode` 🔑 — непорожній
- `String city` — непорожнє

**`LoanContract`**
- `String contractId` 🔑 — непорожній
- `Borrower borrower` — не null
- `BankBranch branch` — не null
- `String loanType` — `MORTGAGE`, `CONSUMER`, `AUTO`, `COMMERCIAL`
- `double principalAmount` — строго > 0 і `<= borrower.getMonthlyIncome() * 60`
- `LocalDate issueDate` — не null; не в майбутньому
- `int termMonths` — від 1 до 360

**`RepaymentScheduleItem`** — 🔑❓
- `LoanContract contract` — не null
- `LocalDate dueDate` — не null; строго пізніша за `contract.getIssueDate()`
- `double amountDue` — строго > 0
- `String status` ✎ — `PENDING`, `PAID`, `OVERDUE`

> **Обчислювані:** `monthlyPayment(LoanContract)` — сума / кількість місяців · `lastDueDate(LoanContract)` — дата останнього платежу

---

## Варіант 13. Автосервіс та СТО

**Car Repair Shop** — автотранспорт, майстри, наряди-замовлення.

**`CarOwner`**
- `String phone` 🔑 — непорожній
- `String name` — непорожнє

**`Mechanic`**
- `String badgeNumber` 🔑 — номер жетона; непорожній
- `String fullName` — непорожнє
- `String specialization` — `DIAGNOSTICS`, `REPAIR`, `MAINTENANCE`, `PAINTING`

**`Vehicle`**
- `String vinCode` 🔑 — непорожній
- `String makeModel` — непорожня
- `int year` — від 1980 до поточного року
- `CarOwner owner` — не null

**`ServiceOrder`** — 🔑❓
- `Vehicle vehicle` — не null
- `Mechanic mechanic` — не null
- `LocalDate startDate` — не null; не в майбутньому
- `LocalDate completionDate` ✎ — може бути null (наряд не завершено); якщо вказано — `>= startDate`
- `double estimatedCost` ✎ — строго > 0
- `String status` ✎ — `OPEN`, `IN_PROGRESS`, `WAITING_FOR_PARTS`, `COMPLETED`

> **Обчислювані:** `workDays(ServiceOrder)` — скільки днів тривав ремонт (−1, якщо ще не завершено) · `vehicleAgeYears(Vehicle)`

---

## Варіант 14. Служба підтримки IT

**IT Ticketing & Help Desk** — заявки, фахівці, облік трудовитрат.

**`ITUser`**
- `String email` 🔑 — непорожня, нижній регістр
- `String fullName` — непорожнє
- `String department` — непорожній

**`Technician`**
- `String techCode` 🔑 — непорожній
- `String name` — непорожнє
- `int skillLevel` ✎ — від 1 до 5

**`ITTicket`**
- `String ticketId` 🔑 — напр. `"INC-404"`; непорожній
- `ITUser reporter` — не null
- `String priority` ✎ — `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`
- `String title` — непорожній
- `LocalDate createdDate` — не null; не в майбутньому
- `LocalDate resolvedDate` ✎ — може бути null; якщо вказано — `>= createdDate`
- `String state` ✎ — `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`

**`WorkLog`** — 🔑❓
- `ITTicket ticket` — не null
- `Technician technician` — не null
- `LocalDate logDate` — не null; `>= ticket.getCreatedDate()`
- `double hoursSpent` — від 0.25 до 24.0

> **Обчислювані:** `resolutionDays(ITTicket)` — скільки днів заявка була у роботі (−1, якщо не закрита) · `workCost(WorkLog)` — години × ставка, ставка залежить від `skillLevel` (константи тримайте в `Utils`)

---

## Варіант 15. Прокат спортивного спорядження

**Sports Equipment Rental** — оренда лиж, сноубордів, велосипедів, прийом майна.

**`SportsCustomer`**
- `String passportNumber` 🔑 — непорожній
- `String fullName` — непорожнє
- `String phone` — непорожній

**`EquipmentItem`**
- `String inventoryNumber` 🔑 — непорожній
- `String name` — непорожня
- `String category` — `SKI`, `SNOWBOARD`, `BICYCLE`, `KAYAK`
- `double pricePerDay` ✎ — строго > 0
- `String condition` ✎ — `NEW`, `GOOD`, `FAIR`, `MAINTENANCE`

**`RentalAgreement`** — 🔑❓
- `SportsCustomer customer` — не null
- `EquipmentItem item` — не null
- `LocalDate rentStartDate` — не null
- `LocalDate rentEndDate` — не null; `>= rentStartDate`
- `double depositPaid` — >= 0

**`ReturnInspection`**
- `String inspectionId` 🔑 — напр. `"INSP-101"`; непорожній
- `RentalAgreement agreement` — не null
- `LocalDate returnDate` — не null; `>= agreement.getRentStartDate()`
- `double damageFee` — >= 0

> **Обчислювані:** `rentalDays(RentalAgreement)` · `totalCost(RentalAgreement)` — дні × ціна за добу

---

## Варіант 16. Онлайн-курси та навчання

**Online Education** — каталог курсів, студенти, викладачі, записи на програми.

**`Student`**
- `String studentId` 🔑 — непорожній
- `String name` — непорожнє
- `String email` — непорожня, нижній регістр
- `LocalDate birthDate` — не null; щонайменше 12 років

**`Instructor`**
- `String instructorCode` 🔑 — непорожній
- `String fullName` — непорожнє
- `String degree` — вчений ступінь; непорожній

**`Course`**
- `String courseCode` 🔑 — напр. `"CS-101"`; непорожній
- `String title` — непорожня
- `Instructor instructor` — не null
- `String difficulty` — `BEGINNER`, `INTERMEDIATE`, `ADVANCED`
- `double price` ✎ — >= 0

**`CourseEnrollment`** — 🔑❓
- `Student student` — не null
- `Course course` — не null
- `LocalDate enrollDate` — не null; не в майбутньому
- `LocalDate completionDate` ✎ — може бути null; якщо вказано — `>= enrollDate`
- `String status` ✎ — `ACTIVE`, `COMPLETED`, `DROPPED`; якщо `COMPLETED`, то `completionDate` не null

> **Обчислювані:** `studyDays(CourseEnrollment)` — скільки днів тривало навчання (−1, якщо не завершено) · `studentAge(Student)`

---

## Варіант 17. Автопарковка

**Parking Management** — заїзд/виїзд, паркувальні зони, розрахунок вартості.

**`Zone`**
- `String zoneCode` 🔑 — непорожній
- `String type` — `VIP`, `UNDERGROUND`, `OVERGROUND`
- `double ratePerHour` ✎ — строго > 0

**`Driver`**
- `String taxId` 🔑 — ІПН або паспорт водія; непорожній
- `String fullName` — непорожнє
- `String phone` — непорожній

**`ParkingTicket`** — 🔑❓
- `String vehiclePlate` — держ. номер; непорожній, обрізається і робиться UPPERCASE. **Зверніть увагу: це не 🔑**
- `Zone zone` — не null
- `Driver driver` — не null
- `LocalDateTime entryTime` — не null; не в майбутньому
- `LocalDateTime exitTime` ✎ — може бути null (авто ще на парковці); якщо вказано — строго пізніший за `entryTime`

**`ParkingPayment`**
- `String paymentId` 🔑 — напр. `"TR-8812"`; непорожній
- `ParkingTicket ticket` — не null
- `double amount` — строго > 0
- `LocalDateTime paymentTime` — не null; `>= ticket.getEntryTime()`

> **Обчислювані:** `parkedHours(ParkingTicket)` — повні години з округленням угору (−1, якщо авто ще не виїхало) · `cost(ParkingTicket)` — години × тариф зони

---

## Варіант 18. Притулок для тварин

**Animal Shelter** — тварини, види, доглядачі, графіки опіки.

**`Species`**
- `String speciesCode` 🔑 — напр. `"SPEC-LION"`; непорожній
- `String commonName` — непорожня
- `String diet` — `CARNIVORE`, `HERBIVORE`, `OMNIVORE`

**`Keeper`**
- `String employeeBadge` 🔑 — непорожній
- `String fullName` — непорожнє
- `LocalDate hireDate` — не null; не раніше `2000-01-01` і не в майбутньому

**`Animal`**
- `String chipNumber` 🔑 — номер мікрочіпа; непорожній
- `String nickname` — кличка; непорожня
- `Species species` — не null
- `LocalDate birthDate` — не null; не в майбутньому
- `String health` ✎ — `HEALTHY`, `UNDER_TREATMENT`, `QUARANTINE`

**`CareSchedule`** — 🔑❓
- `Animal animal` — не null
- `Keeper keeper` — не null
- `LocalDate startDate` — не null; `>= animal.getBirthDate()` і `>= keeper.getHireDate()`
- `LocalDate endDate` ✎ — може бути null (опіка триває); якщо вказано — `>= startDate`
- `int feedingsPerDay` ✎ — від 1 до 6

> **Обчислювані:** `animalAgeMonths(Animal)` · `careDays(CareSchedule)` — тривалість опіки (−1, якщо триває)

---

## Варіант 19. Оренда автомобілів

**Car Rental** — автопарк, клієнти, оренди, оплати.

**`Car`**
- `String licensePlate` 🔑 — непорожній, обрізається і робиться UPPERCASE
- `String model` — непорожній
- `int year` — від 1900 до поточного року
- `int mileage` ✎ — від 0 до 2 000 000
- `String status` ✎ — `AVAILABLE`, `RENTED`, `MAINTENANCE`, `RESERVED`

**`Customer`**
- `String driverLicense` 🔑 — непорожній
- `String firstName`, `String lastName` — непорожні
- `LocalDate birthDate` — не null; щонайменше 18 років

**`Branch`**
- `String name` 🔑 — напр. `"Kyiv-Central"`; непорожня
- `String location` — адреса; непорожня

**`Rental`** — 🔑❓
- `Car car` — не null
- `Customer customer` — не null
- `Branch branch` — не null
- `LocalDate startDate` — не null
- `LocalDate endDate` — не null; `>= startDate`

**`Payment`**
- `String paymentId` 🔑 — напр. `"PAY-1001"`; непорожній, приходить ззовні
- `Rental rental` — не null
- `double amount` — строго > 0
- `LocalDate paymentDate` — не null; не раніша за `rental.getStartDate()`
- `String paymentMethod` — `CREDIT_CARD`, `DEBIT_CARD`, `CASH`, `ONLINE`

> **Обчислювані:** `rentalDays(Rental)` · `carAgeYears(Car)` — поточний рік мінус рік випуску

---

## Варіант 20. Музичний стримінговий каталог

**Music Streaming Catalog** — виконавці, альбоми, треки, плейлисти користувачів.

**`Artist`**
- `String artistCode` 🔑 — непорожній
- `String stageName` — сценічний псевдонім; непорожній
- `String country` — непорожня
- `int debutYear` — від 1900 до поточного року

**`MusicUser`**
- `String accountEmail` 🔑 — непорожня, нижній регістр
- `String username` — непорожній
- `String subscription` ✎ — `FREE`, `PREMIUM`, `FAMILY`

**`Album`**
- `String upcCode` 🔑 — штрих-код альбому; непорожній
- `String title` — непорожня
- `Artist artist` — не null
- `String genre` — `ROCK`, `POP`, `JAZZ`, `CLASSICAL`, `ELECTRONIC`, `HIPHOP`
- `LocalDate releaseDate` — не null; не в майбутньому; рік релізу не раніший за `artist.getDebutYear()`

**`Track`**
- `String isrcCode` 🔑 — непорожній
- `Album album` — не null
- `String title` — непорожня
- `int durationSeconds` — від 1 до 3600
- `int trackNumber` — від 1 до 500

**`PlaylistTrack`** — 🔑❓
- `MusicUser user` — не null
- `Track track` — не null
- `LocalDate addedDate` — не null; не раніша за `track.getAlbum().getReleaseDate()`

> **Обчислювані:** `formatDuration(Track)` — тривалість у вигляді `"3:45"` · `trackAgeYears(Track)` — скільки років від релізу альбому
