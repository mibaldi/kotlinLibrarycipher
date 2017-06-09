package com.mibaldi.ciferlibrary

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.*


var secretToken = ""
val ALGORITMO_CIFRADO = "AES"
val ALGORITMO_TRANSFORMACION_AES = "AES/CBC/PKCS5Padding"

val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()
val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

fun <T> cifer(token:String, item:T,clazz: Class<T>) : T {
    secretToken = token


    val jsonString = gson.toJson(item)
    val jsonObject = JSONObject(jsonString)

    val ciferJsonObject = ciferJsonObject(jsonObject)
    val itemCifer = gson.fromJson(ciferJsonObject.toString(), clazz)

    return itemCifer
}
fun ciferJsonObject(jsonObject:JSONObject) :JSONObject {

    val jsonObjectCifer = JSONObject();
    for (key in jsonObject.keys()) {
        val value = jsonObject.get(key)
        if (value is JSONArray) {
            val ciferJsonArray = ciferJsonArray(value)
            jsonObjectCifer.put(key,ciferJsonArray)
        }
        else if (value is JSONObject) {
            jsonObjectCifer.put(key, ciferJsonObject(value))
        }
        else if (value is String){

            try {
                simpleDateFormat.parse(value)
                jsonObjectCifer.put(key, value)
            }catch (e:ParseException){
                jsonObjectCifer.put(key, encrypt(value))
            }

        }else {
            //TODO fijarse en fechas, booleanos
            jsonObjectCifer.put(key, value)
        }
    }
    return jsonObjectCifer
}
fun ciferJsonArray(jsonArray: JSONArray) :JSONArray {

    val jsonArrayCifer = JSONArray()

    for (i in 0..(jsonArray.length() - 1)) {
        val item = jsonArray.get(i)
        if (item is JSONArray){
            jsonArrayCifer.put(i,ciferJsonArray(item))
        }else if (item is JSONObject){
            jsonArrayCifer.put(i,ciferJsonObject(item))
        }else if (item is String) {

            try {
                simpleDateFormat.parse(item)
                jsonArrayCifer.put(i, item)
            }catch (e:ParseException){
                jsonArrayCifer.put(i, encrypt(item))
            }
        }
        else {
            //TODO fijarse en fechas, booleanos
            jsonArrayCifer.put(i, item)
        }
    }
    return jsonArrayCifer
}

fun encrypt(normalValue: String):String{
    var textoCifrado= ""
    val cifraDatosAES = cifraDatosAES(secretToken.toByteArray(), normalValue.toByteArray())
    textoCifrado = CodificadorBase64.encodeToString(cifraDatosAES)
    return textoCifrado
}


fun <T> decifer(token:String, item:T,clazz: Class<T>) : T {
    secretToken = token
    val jsonString = gson.toJson(item)
    val jsonObject = JSONObject(jsonString)

    val deciferJsonObject = deciferJsonObject(jsonObject)
    val itemDecifer = gson.fromJson(deciferJsonObject.toString(), clazz)

    return itemDecifer
}
fun deciferJsonObject(jsonObjectCifer:JSONObject) :JSONObject {

    val jsonObject = JSONObject();
    for (key in jsonObjectCifer.keys()) {
        val value = jsonObjectCifer.get(key)
        if (value is JSONArray) {
            val ciferJsonArray = deciferJsonArray(value)
            jsonObject.put(key,ciferJsonArray)
        }
        else if (value is JSONObject) {
            jsonObject.put(key,deciferJsonObject(value))
        }
        else if (value is String){

            try {
                simpleDateFormat.parse(value)
                jsonObject.put(key, value)
            }catch (e:ParseException){
                jsonObject.put(key, decrypt(value))
            }
        }else {
            //TODO fijarse en fechas, booleanos
            jsonObject.put(key, value)
        }
    }
    return jsonObject
}
fun deciferJsonArray(jsonArrayCifer: JSONArray) :JSONArray {

    val jsonArray = JSONArray()

    for (i in 0..(jsonArrayCifer.length() - 1)) {
        val item = jsonArrayCifer.get(i)
        if (item is JSONArray){
            jsonArray.put(i,deciferJsonArray(item))
        }else if (item is JSONObject){
            jsonArray.put(i,deciferJsonObject(item))
        }else if (item is String) {

            try {
                simpleDateFormat.parse(item)
                jsonArray.put(i, item)
            }catch (e:ParseException){
                jsonArray.put(i, decrypt(item))
            }
            jsonArray.put(i, decrypt(item))
        }
        else {
            //TODO booleanos
            jsonArray.put(i, item)
        }
    }
    return jsonArray
}
fun decrypt(criptValue:String):String {
    val textoClaro = CodificadorBase64.decode(criptValue)
    val textoDesCifrado = descifraDatosAES(secretToken.toByteArray(), textoClaro)
    return String(textoDesCifrado)
}

@Throws(Exception::class)
fun cifraDatosAES(keyBytes: ByteArray, datos: ByteArray): ByteArray {

    val paramSpec = IvParameterSpec(keyBytes)
    val key = SecretKeySpec(keyBytes, ALGORITMO_CIFRADO)
    val cipher = Cipher.getInstance(ALGORITMO_TRANSFORMACION_AES)
    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec)
    return cipher.doFinal(datos)
}
/**
 * Descifra datos cifrados con AES
 * @param keyBytes bytes de la clave
 * *
 * @param datos datos a descifrar
 * *
 * @return un array de bytes con los datos descifrados
 * *
 * @throws Exception si no puede realizar la operacin de cifrado
 */
@Throws(Exception::class)
fun descifraDatosAES(keyBytes: ByteArray, datos: ByteArray): ByteArray {
    val paramSpec = IvParameterSpec(keyBytes)
    val key = SecretKeySpec(keyBytes, ALGORITMO_CIFRADO)
    val cipher = Cipher.getInstance(ALGORITMO_TRANSFORMACION_AES)
    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec)
    return cipher.doFinal(datos)
}