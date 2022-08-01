# android-wp
 Android WordPress App

# Описание

Целью данной работы является разработка Android-приложения с push-уведомлениями для сайта на WordPress. Приложение обеспечивает посетителям быстрый доступ к публикациям на сайте, навигацию по сайту и оповещения читателей о новых публикациях посредством push-уведомлений в приложении.

В рамках задание создан сайт-портфолио под управлением одной из самых популярных систем управления контентом WordPress. Описаны результаты создания клиент-серверного приложения на языке Java в среде разработки Android Studio. Мы обеспечили взаимодействие приложения с сайтом и как настроили push-уведомления о новых публикациях на сайте. Также мы реализовали монетизацию приложения за счет рекламы и внутренних покупок в приложении. Итоговое приложение готово к публикации в маркете Google Play.

# Описание сайта на WordPress

Для реализации сайта-портфолио на WordPress используется платформа Yandex Cloud с привязкой домена. Сайт содержит записи с описанием личных проектов и ссылкой на них в GitHub. Скриншот главной страницы представлена на рисунке 1.

**WordPress-сайт:** http://wp.olimpiev.site/

![](https://github.com/chiziwe-2-0/android-wp/pics_for_readme/unnamed-1.png)

Рисунок 1. Главная страница WordPress-сайта

**Используемые плагины:**

- Disqus for WordPress - помогает повысить вовлеченность пользователей и поддерживает синхронизацию комментариев с базой данных для удобного резервного копирования.
- Github Embed - интеграция проектов GitHub на страницы для автоматической загрузки информация о проекте на сайт.
- JSON API - RESTful API для WordPress.

1.
# Описание Android-приложения


**Репозиторий с исходным кодом приложения:** https://github.com/chiziwe-2-0/android-wp

Приложение создано для для удобного чтения публикаций посетителями. В процессе разработки реализована возможность гибко настраивать способ навигации по контенту и оформление приложения. В приложении реализована возможность поиска по материалам сайта, возможность добавления материалов в избранное для быстрого доступа к ним. Пользователям доступна возможность комментировать статьи сайта через платформу обсуждений Disqus. Реализована возможность шаринга материалов через соцсети или другие приложения. Также реализована монетизация приложения с помощью рекламных баннеров. Второй вариант монетизации — отключение рекламы и открытие статей с ограниченным доступом через In-app Billing (покупки в приложении). Приложение разработано в соответствии с требованиями Material Design. Навигация по приложению реализована через Navigation Drawer.

Основным файлом конфигурации и маппинга нашего приложения с WordPress-сайтом является config.json, его пример представлена на рисунке 2.

![](RackMultipart20220801-1-2k8sgz_html_56c308533486729e.png)

Рисунок 2. Пример файла config.json

Опишем некоторые неочевидные параметры конфигурации:

- ключ &quot;iap&quot; отвечает за скрытие контента за paywall - позволяет запретить доступ к рубрике сайта без оплаты при значении true;
- ключ &quot;tabs&quot; содержит массив с параметрами рубрики сайта, где &quot;arguments&quot; содержат адрес сайта, название рубрики. адрес сайта, подключенного с помощью Disqus;
- ключ &quot;submenu&quot; позволяет добавить некликабельное название подменю для навигации в приложении, пример можно увидеть на рисунке 3.

![](RackMultipart20220801-1-2k8sgz_html_449fb412a22c8b2d.png)

Рисунок 3. Подменю &quot;Все записи&quot;

В приложении реализовано боковое меню для навигации по рубрикам сайта (рисунок 3). При открытии рубрики, спрятанной за paywall пользователь получает информацию о заблокированном содержимом, откуда есть возможность перейти к оплате, нажав на &quot;Отключение рекламы&quot; (рисунок 4).

![](RackMultipart20220801-1-2k8sgz_html_3c6a4ef07f1e6bc3.png)

Рисунок 4. Окно с информацией о блокировке содержимого

Главный экран приложения содержит список всех статей на сайте, а именно заголовок и дату публикации (рисунок 5). Из данного экрана пользователь может обновить список статей, перейти к поиску по статьям и к открытию меню &quot;три точки&quot;.

![](RackMultipart20220801-1-2k8sgz_html_c095442649df4300.png)

Рисунок 5. Главный экран приложения

При открытии статьи пользователю доступен весь ее текст с кликабельными ссылками с возможностью перехода по ним внутри приложения (рисунок 6-7). После перехода к статье пользователь может добавить ее в список избранных, реализованный с помощью СУБД SQLite. Также пользователь может поделиться ссылкой (рисунок 8).

![](RackMultipart20220801-1-2k8sgz_html_702f3dd6d704dee2.png)

Рисунок 6. Пример статьи

![](RackMultipart20220801-1-2k8sgz_html_9d6a43b8845fc9e6.png)

Рисунок 7. Переход по ссылке в статье

![](RackMultipart20220801-1-2k8sgz_html_a839f35fe0790641.png)

Рисунок 8. Реализация &quot;Поделиться&quot;

Пользователь может как видеть счетчик количества комментариев (рисунок 6), так и просматривать комментарии к статье. При переходе к комментариям пользователю также доступно добавление новых комментариев с помощью Disqus (рисунок 9).

![](RackMultipart20220801-1-2k8sgz_html_4400530e5a3f9d43.png)

Рисунок 9. Просмотр и редактирование комментариев

Также в бесплатной версии на всех экранах приложения присутствует реклама: баннер снизу и открываемый при нескольких переходах по рубрикам отдельный рекламный экран (количество переходов настраиваемое, по умолчанию - 5).

При переходе к меню &quot;три точки&quot; пользователю доступны список избранных записей (рисунок 10) и настройки приложения (рисунок 11).

![](RackMultipart20220801-1-2k8sgz_html_8694c8d29e24f2cd.png)

Рисунок 10. Список избранных записей

![](RackMultipart20220801-1-2k8sgz_html_4d089cd1f7c02d2f.png)

Рисунок 11. Настройки приложения

В настройках доступно:

1. Параметр отображения бокового меню при старте;
2. Изменение размера текста в статьях;
3. Информация о приложении;
4. Переход к оцениванию приложения в Google Play;
5. Отключение рекламы (переход к оплате).

Для реализации push-уведомлений в приложении используем связку Firebase-OneSignal-Zapier. По умолчанию настроена отправка уведомлений при публикации новой записи с интервалом обновления - 5 минут. Пример настройки Zapier представлен на рисунке 12, пример уведомления - на рисунке 13.

![](RackMultipart20220801-1-2k8sgz_html_eb5ed01559bdd3ea.png)

Рисунок 12. Настройка Zapier

![](RackMultipart20220801-1-2k8sgz_html_4731574ea90966f7.png)

Рисунок 13. Уведомление в приложении

# Заключение

В процессе выполнения итогового задания нами был создан сайт на CMS WordPress и мобильное Android-приложение для сайта. В процессе было изучено и реализовано на практике:

- создание сайта на CMS WordPress;
- создание Android-приложение для сайта;
- интеграция публикации с сайта с приложением;
- реализована навигация и поиск по материалам сайта в приложении;
- настроены push-уведомления в приложении о новых публикациях на сайте;
- встроены рекламные баннеры AdMob для монетизации приложения;
- реализована монетизация приложения через In-app Billing (покупки в приложении).

В процессе выполнения задания достигнута основная цель работы - разработка Android-приложения с push-уведомлениями для сайта на WordPress.
