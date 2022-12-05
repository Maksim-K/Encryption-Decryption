package encryptdecrypt

import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    // task1()
    // task2()
    // task3()
    task4(args.toList())
}

fun task4(args: List<String>) {
    var (mode, key, data, alg) = listOf<String>("enc", "0", "", "shift")
    var (inFileName, outFileName)  = listOf<String>("", "")
    var isOutToFile = true
    var isInFromFile = false

    try {
        val li = args.listIterator()
        while (li.hasNext()) {
            when(li.next()) {
                "-mode" -> mode = li.next()
                "-key" -> key = li.next()
                "-data" -> data = li.next()
                "-in" -> inFileName = li.next()
                "-out" -> outFileName = li.next()
                "-alg" -> alg = li.next()
            }
        }
    } catch (e: Exception) {
        println("Error: exception ${e.message}")
        exitProcess(0)
    }

    if (data == "") {
        isInFromFile = true
    } else {
        if (data.first() == '"') data = data.substring(1)
        if (data.last() == '"') data = data.substring(0, data.length - 1)
    }

    if (outFileName == "") isOutToFile = false

    if (isInFromFile) {
        try {
            data = File(inFileName).readText()
        } catch (e: FileNotFoundException) {
            println("Error: input file \"$inFileName\" not found")
            exitProcess(0)
        }
    }
    if (Encrypt.ALG.valueOf(alg.uppercase()) !in Encrypt.ALG.values()) alg="shift"

    when (mode) {
        "enc" -> {
            val result = Encrypt(key.toInt()).getEncrypted(data, Encrypt.ALG.valueOf(alg.uppercase()))
            if (isOutToFile) {
                File(outFileName).writeText(result)
            } else println(result)
        }
        "dec" -> {
            val result = Encrypt(key.toInt()).getDecrypted(data, Encrypt.ALG.valueOf(alg.uppercase()))
            if (isOutToFile) {
                File(outFileName).writeText(result)
            } else println(result)
        }
        else -> {
            println("Error: incorrect action")
            exitProcess(0)
        }
    }
}

fun task3() {
    val (action, string, key) = listOf<String>(readln(), readln(), readln())
    when (action) {
        "enc" -> println(Encrypt(key.toInt()).getEnc3(string))
        "dec" -> println(Encrypt(key.toInt()).getDec3(string))
        else -> println("incorrect action")
    }
}

fun task2() {
    val stringToEncrypt = readln()
    val key = readln().toInt()
    println(Encrypt(key).getEnc2(stringToEncrypt))
}

fun task1() {
    val stringToEncrypt = "we found a treasure!" // task1
    println(Encrypt().getEncrypted1(stringToEncrypt))
}

class Encrypt(private val key: Int = 0) {
    enum class ALG {
        SHIFT,
        UNICODE,
    }

    fun getEncrypted(s: String, alg: ALG) = when (alg) {
        ALG.SHIFT -> getEnc2(s)
        ALG.UNICODE -> getEnc3(s)
    }

    fun getDecrypted(s: String, alg: ALG) = when (alg) {
        ALG.SHIFT -> getDec2(s)
        ALG.UNICODE -> getDec3(s)
    }

    fun getEnc3(s: String): String = s
            .map {encChar3(it, key)}
            .joinToString("")

    fun getDec3(s: String): String = s
            .map {decChar3(it, key)}
            .joinToString("")

    private fun encChar3(c: Char, shift: Int): Char = (c.code + shift).toChar()
    private fun decChar3(c: Char, shift: Int): Char = (c.code - shift).toChar()

    fun getEnc2(s: String): String = s
            .map {encChar2(it, key)}
            .joinToString("")

    fun getDec2(s: String): String = s
            .map {decChar2(it, key)}
            .joinToString("")

    private fun encChar2(c: Char, shift: Int): Char {
        val alphabetEnglishLower = "abcdefghijklmnopqrstuvwxyz"
        val codeBase = when (c) {
            in alphabetEnglishLower -> alphabetEnglishLower
            in alphabetEnglishLower.uppercase() -> alphabetEnglishLower.uppercase()
            else -> ""
        }
        if (codeBase == "") return c
        return ((codeBase.indexOf(c) + shift) % codeBase.length + codeBase.first().code).toChar()
    }
    private fun decChar2(c: Char, shift: Int): Char {
        val alphabetEnglishLower = "abcdefghijklmnopqrstuvwxyz"
        val codeBase = when (c) {
            in alphabetEnglishLower -> alphabetEnglishLower
            in alphabetEnglishLower.uppercase() -> alphabetEnglishLower.uppercase()
            else -> ""
        }
        if (codeBase == "") return c

        val result = (codeBase.indexOf(c) - shift) % codeBase.length + codeBase.first().code
        if (result < codeBase.first().code) {
            return (codeBase.length + result).toChar()
        }
        return result.toChar()
    }

    fun getEncrypted1(s: String): String {
        val myList = emptyList<Char>().toMutableList()
        for (word in s) myList.add(encryptChar1(word))
        return myList.joinToString("")
    }

    private fun encryptChar1(c: Char): Char = when (c) {
        in 'a'..'z' -> {
            ('z'.code + 'a'.code - c.code).toChar()
        }
        else -> c
    }

}
