package com.sgdc.roguelike

class Player (){
    var health: Int = 0
    var maxHealth : Int = 0
    var att : Int = 0
    var def : Int = 0
    var exp : Float = 0.0f
    var maxExp : Float = 0.0f
    var level: Int = 1

    constructor(health:Int,maxHealth:Int, att:Int, def:Int, exp:Float, maxExp:Float, level:Int):this(){
        this.exp = exp
        this.att = att
        this.def = def
        this.health = health
        this.maxHealth = maxHealth
        this.maxExp = maxExp
        this.level = level
    }
    constructor(health:Int, att:Int, def:Int):this(){
        this.health = health
        this.att = att
        this.def = def
    }

    fun levelUp(){
        this.exp = 0.0f
        this.level += 1
        this.maxExp = (this.maxExp * 1.25f).toFloat()
    }
}