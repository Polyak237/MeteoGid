# MeteoGid
Мобильное приложение для информарования о погоде. 
Приложение предназначено для использования любым человеком. На главной странице предоставлена краткая информация о погоде на сегодняшний день и несколько последующих дней. При нажатии на кнопку "Подробнее" происходит переход на страницу с более подробным описанием погоды, а также кратким почасовым прогнозом на этот же день. 

# Инструкция пользователя
При первом открытии информация будет отсутствовать.  

![](https://github.com/Polyak237/MeteoGid/blob/d7b5eacc386f196bab94586a359ca4e28c026238/%D0%9D%D0%B0%D1%87.%20%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0.jpg)

Для её отображения в верхнее поле необходимо ввести название города, после чего нажать на кнопку ввода. Через некоторое время страница обновится и будет выведена краткая информация на сегодня, а также прогноз на неделю вперёд. Страницу можно прокручивать вниз. 

![](https://github.com/Polyak237/MeteoGid/blob/d7b5eacc386f196bab94586a359ca4e28c026238/%D0%9E%D0%B1%D0%BD%D0%BE%D0%B2%D0%BB.%20%D0%BD%D0%B0%D1%87.%20%D1%81%D1%82%D1%80..jpg)

Для получения более подробных данных о нынешней погоде необходимо перейти на следующую страницу, нажав на кнопку "Подробнее..." в центре экрана. На следующей странице будет отображён более полный перечень информации о погоде, а также почасовой прогноз на 12 часов вперёд. Страницу также можно прокручивать.

![](https://github.com/Polyak237/MeteoGid/blob/d7b5eacc386f196bab94586a359ca4e28c026238/%D0%9F%D0%BE%D0%B4%D1%80%D0%BE%D0%B1%D0%BD%D0%B5%D0%B5.jpg)

# Информация для разработчиков
Проект создан в среде Android Studio. 
Было использовано два лэйаута: activity_main и activity_podrobno, представленные в виде XML-файлов. 
- В первом размещаются различные элементы интерфейса, используемые на главной странице, а именно: 
    - Поле для ввода названия города (id: city)
    - Кнопка для сохранения названия города и запуска парсера по этому названию (id: btnSaveCity)
    - Картинка, отражающая состояние погоды (id: Pogoda)
    - Поле для вывода сводки на сегодняшний день (id: Waiting)
    - ScrollView, в котором находится RelativeLayout с шестю CardView, в каждом из которых отображается дата и краткий прогноз. 

![](https://github.com/Polyak237/MeteoGid/blob/9ca141fe5e73538141b2e837ce0f1dceb76aaa57/Activity_Main.PNG)

- Функции, руководящие процессами этого лэйаута, расположены в классе MainActivity. Все методы прокомментированны.
 
![](https://github.com/Polyak237/MeteoGid/blob/9ca141fe5e73538141b2e837ce0f1dceb76aaa57/MainActivity.PNG)

- Во втором находится подробный подробная информация о нынешней ситуации, а именно:
    - Дата сегодняшнего дня
    - Давление
    - Влажность
    - Скорость ветра
    - УФ-индекс
    - Скорость ветра. Функции этой страницы находятся в классе Podrobno
Также для работы с Json-объектами был создан класс JsonReader, необходимый для базовой работы с объектами


# Функциональные требования к проекту (отображают процессы, реализованные по ходу создания проекта)
Проект представляет из себя метеорологическое приложение, информирующее пользователя о температуре и погоде

- На главной странице будет предоставлена возможность выбрать свой город
- На следующей странице будет показана краткая информация о температуре на данный момент, а также продемонстрирована краткая погода на два следующих дня
- При нажатии на кнопку "Подробнее" будет открыта страница с расширенной информацией о нынешней погоде, включающая в себя:
    - Давление
    - Влажность
    - Скорость ветра
    - Ультрафиолетовый индекс
    - Краткий почасовой прогноз погоды до конца дня

# Макет приложения
![Макет](https://github.com/Polyak237/MeteoGid/blob/e124a767b30f46102fe7ca4d185f7fbfd523c183/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA.png)
