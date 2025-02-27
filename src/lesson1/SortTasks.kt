@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.Exception

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Время работы - O(2n^2), размер требуемой памяти - O(3*n)
 */
fun sortTimes(inputName: String, outputName: String) {
    val fileWithTimes = File(inputName)
    if (fileWithTimes.notExists())
        return

    val midAmTimes = mutableListOf<String>()
    val amTimes = mutableListOf<String>()
    val midPmTimes = mutableListOf<String>()
    val pmTimes = mutableListOf<String>()

    fileWithTimes.forEachLine {
        val time = it.split(' ')
        if (time.size != 2)
            throw Exception("WrongFormatException")

        val (timeValue, postfix) = time
        val (notInFormat, isBeforeMidday, isTwelve) = timeNotInFormat(timeValue, postfix)
        if (notInFormat)
            throw Exception("WrongFormatException")

        if (isBeforeMidday)
            if (isTwelve) midAmTimes.add(it)
            else amTimes.add(it)
        else
            if (isTwelve) midPmTimes.add(it)
            else pmTimes.add(it)
    }

    midAmTimes.sort()
    amTimes.sort()
    midPmTimes.sort()
    pmTimes.sort()

    midAmTimes += amTimes + midPmTimes + pmTimes

    File(outputName).bufferedWriter().use {
        for (times in midAmTimes) {
            it.write(times.plus("\n"))
        }
    }
}

fun File.notExists() = !this.exists()

fun timeNotInFormat(timeValue: String, postfix: String): Triple<Boolean, Boolean, Boolean> {
    var isTwelve = false
    var isBeforeMidday = false

    val default = Triple(true, isBeforeMidday, isTwelve)

    if (postfix == "AM")
        isBeforeMidday = true
    else if (postfix != "PM")
        return default

    val timeValues = timeValue.split(':')
    if (timeValues.size != 3)
        return default

    val (hours, minutes, seconds) = timeValues
    if (hours.length != 2 || minutes.length != 2 || seconds.length != 2)
        return default

    var isHours = true
    hours.toInt().also {
        if (it == 12) isTwelve = true
        else if (it !in 0..11) isHours = false
    }

    val isMinutes = minutes.toInt() in 0..60
    val isSeconds = seconds.toInt() in 0..60
    if (isHours && isMinutes && isSeconds)
        return Triple(false, isBeforeMidday, isTwelve)

    return default
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    val fileWithAddresses = File(inputName)
    if (fileWithAddresses.notExists())
        return

    sortAddressesByKirill(fileWithAddresses, outputName)
}

fun sortAddressesByAnastasiya(addresses: File, outputName: String) {
    val addresses = addresses.bufferedReader().readLines()
    val sortedByAddresses = addresses.sortedBy { it.split('-')[1] }
    val uniqueAddresses = mutableListOf<String>()

    for (i in 0 until sortedByAddresses.size - 1) {
        val currentAddress = sortedByAddresses[i].split('-')
        val nextAddress = sortedByAddresses[i + 1].split('-')
        if (currentAddress[1] == nextAddress[1])
            TODO()
    }
}

fun sortAddressesByKirill(addresses: File, outputName: String) {
    val answer = mutableMapOf<String, MutableList<String>>()
//    val answer = mutableMapOf<String, List<SortedSet<String>>>()
    addresses.bufferedReader().forEachLine {
        answer.add(it)
    }

    answer.forEach {
        it.value.sort()
    }

    File(outputName).bufferedWriter().use {
        val sortedAddresses = answer.keys.sortedWith(
            compareBy(
                { it.split(' ')[0] },
                { it.split(' ')[1].toInt() }
            )
        )

        for (address in sortedAddresses) {
            val fullNames = answer[address]!!

            it.write(address.plus(" - "))

            if (fullNames.size != 1)
                for (indexOfFullName in 0 until fullNames.size - 1)
                    it.write(fullNames[indexOfFullName].plus(", "))

            it.write(fullNames.last())
            it.write("\n")
        }
    }
}

fun MutableMap<String, MutableList<String>>.add(innerValue: String) {
    val (fullName, address) = innerValue.split(" - ")
    if (this.contains(address))
        this[address]!!.add(fullName)
    else
        this[address] = mutableListOf(fullName)
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

