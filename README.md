| <h1><b>Brina: Text-Editor-with-AI</b></h1> | &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;|
|---|---|
| Редактор документов с возможностью записи текста с помощью голоса. Поддерживает чат gpt, который сможет ответить на вопрос, а также подкорректировать текст, записанный голосовым вводом, по запросу из выпадающего пользователю списка. Также есть возможность создавать папки с документами и давать права доступа определенным людям для просмотра документов. | <img src="https://github.com/B-E-D-A/Text-Editor-with-AI/assets/112130616/cd3542ad-55fd-424e-954c-76d0d1f34327" alt="small-icon" width="200" height="200"> |


### Возможности пользователя:

- Базовый набор функций редактора документов
- Записывать текст голосом
- Использовать чат gpt
- Создавать папки с правами доступа для определенных людей
- Сохранение документов в разных форматах (pdf, txt, docx)

### Инструкция по установке приложения

- скачайте javafx 17.0.10 -  https://gluonhq.com/products/javafx/, распакуйте zip


- File - Project Structure - Libraries : New Project Library типа Java - в папках находите распакованный zip и выбирайте из него папку lib


- Edit Configurations - Add new configuration - Application:

Name = `Main`,
module not specified = `19`,
-cp \<no module\> = `Text-Editor-with-AI.main`, 
Main Class = `org.hse.brina.Main`

Modify options —> Add VM options: `--module-path "path\openjfx-17.0.10_windows-x64_bin-sdk\javafx-sdk-17.0.10\lib" --add-modules javafx.controls,javafx.fxml` , где в кавычках надо указать путь к папке lib у скачанной библиотеки javafx

- запускаем `Server.java`, далее `Main.java` (Main, который настоили)

при запуске в первый раз попросит ввести путь до базы данных - надо вставить абсолютный путь до файла `Text-Editor-with-AI/database/database.db`

### Текущий вид приложения

| | |
|---|---|
| <img src="https://github.com/B-E-D-A/Text-Editor-with-AI/assets/112130616/cf516f22-2380-4fa0-a820-64e6cd8436b6" alt="sign-in" width="447" height="319"> | <img src="https://github.com/B-E-D-A/Text-Editor-with-AI/assets/112130616/9c08785a-ee25-4cfd-9d7a-912c0035ba38" alt="sign-up" width="447" height="319"> |
| <img src="https://github.com/B-E-D-A/Text-Editor-with-AI/assets/112130616/cb459d13-a22a-4326-837a-5c286ef02602" alt="main-window" width="447" height="319"> | <img src="https://github.com/B-E-D-A/Text-Editor-with-AI/assets/112130616/b50afed6-8ece-4e93-888f-66563fa629ba" alt="editor" width="447" height="319"> |

Палитра:

&nbsp; &nbsp; &nbsp; ![#101d2f](https://placehold.co/15x15/101d2f/101d2f.png) &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; ![#194f93](https://placehold.co/15x15/194f93/194f93.png)  &nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp; ![#3d6dac](https://placehold.co/15x15/3d6dac/3d6dac.png) &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;![#80aad7](https://placehold.co/15x15/80aad7/80aad7.png) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;![#f6ecde](https://placehold.co/15x15/f6ecde/f6ecde.png) &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; ![#e0850c](https://placehold.co/15x15/e0850c/e0850c.png)

`#101d2f` `#194f93` `#3d6dac` `#80aad7` `#f6ecde` `#e0850c`
