# Исправления


## 1. Постеры не загружались на экранах Popular и Upcoming


На экранах **Popular** и **Upcoming** отображались карточки фильмов (название, рейтинг), но вместо постера — иконка «изображение недоступно». На экране **Details** картинки загружались нормально.

### Причина

Проблема была в компоненте `MovieItem` (`app/src/main/java/com/example/moviesapp/ui/components/MovieItem.kt`), который рисует карточку в сетке.

**а) Не использовалось свойство `.state` у Coil**

`rememberAsyncImagePainter` возвращает объект `AsyncImagePainter`, а не состояние загрузки. Состояние (`Loading`, `Success`, `Error`) лежит в свойстве `.state`.

В `MovieItem` проверки выглядели так:

```kotlin
if (imageState is AsyncImagePainter.State.Success) { ... }
```

но `imageState` был самим painter’ом, без `.state`. Условия **никогда не выполнялись**, изображение в ветку `Success` не попадало.

На экране деталей всё работало, потому что там уже было правильно:

```kotlin
rememberAsyncImagePainter(...).state
```

**б) Неверная сборка URL и поле для картинки**

| Было в `MovieItem` | Как на Details (работало) |
|---|---|
| `movie.backdrop_path` | `poster_path` |
| без `removePrefix("/")` | `posterPath.removePrefix("/")` |

TMDB отдаёт пути вида `/abc123.jpg`. Базовый URL уже заканчивается на `/`:

`https://image.tmdb.org/t/p/w500/`

Для карточек в списке нужен **постер** (`poster_path`), а не фон (`backdrop_path`).

### Что изменено

В `MovieItem.kt`:

```kotlin
val posterPath = movie.poster_path
val imageState = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .data(MovieApi.IMAGE_BASE_URL + posterPath.removePrefix("/"))
        .size(Size.ORIGINAL)
        .build()
).state
```

- добавлен `.state` для корректной обработки `Success` / `Error`;
- для загрузки используется `poster_path`;
- путь нормализуется через `removePrefix("/")`, как на экране деталей.

---

## 2. Список фильмов не подгружался из API (категории)


Запросы к TMDB для популярных и предстоящих фильмов не проходили как ожидалось: данные с сервера не приходили, приложение могло опираться только на кэш или показывать пустые/неполные списки.

### Причина

В объекте `Category` (`app/src/main/java/com/example/moviesapp/utils/Category.kt`) строковые значения констант были с **заглавной буквы**:

```kotlin
// было (неверно для TMDB API)
const val POPULAR = "Popular"
const val UPCOMING = "Upcoming"
```

Эти строки подставляются в путь Retrofit-запроса:

```
GET https://api.themoviedb.org/3/movie/{category}
```

API The Movie Database ожидает **строчные** имена категорий:

- `popular` → `.../movie/popular`
- `upcoming` → `.../movie/upcoming`

При `"Popular"` или `"Upcoming"` сервер получает неверный путь, ответ не соответствует ожидаемому, и загрузка списков ломается.

### Что изменено

Исправлены значения констант (вручную):

```kotlin
object Category {
    const val POPULAR = "popular"
    const val UPCOMING = "upcoming"
}
```