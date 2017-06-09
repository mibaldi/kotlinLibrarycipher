package com.mibaldi.ciferlibrary


fun<T> ciferObject(token:String,item:T,clazz: Class<T>):T{
    return cifer(token,item,clazz)
}
fun<T> deciferObject(token:String,item:T,clazz: Class<T>):T{
    return decifer(token,item,clazz)
}