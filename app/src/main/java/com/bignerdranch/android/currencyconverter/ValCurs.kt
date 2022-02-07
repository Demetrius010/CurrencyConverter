package com.bignerdranch.android.currencyconverter

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false) // Like POJO classes for xml data
data class ValCurs constructor(
    @field:ElementList(name = "Valute", inline = true, required = false, entry = "Valute", empty = true)
    var valutes: MutableList<Valute>? = null,
    @field:Attribute(name = "Date")
    var date: String = "",
    @field:Attribute(name = "name")
    var name: String = "")


@Root(name = "Valute", strict = false)
data class Valute constructor (
    @field:Attribute(name = "ID", required = true)
    var id: String = "",
    @field:Element(name = "NumCode", required = true)
    var numCode: Int = 0,
    @field:Element(name = "CharCode", required = true)
    var charCode: String = "",
    @field:Element(name = "Nominal", required = true)
    var nominal: Int = 0,
    @field:Element(name = "Name", required = true)
    var name: String = "",
    @field:Element(name = "Value", required = true)
    var value: String = ""){

    fun getValue(): Double {
        return value.replace(',', '.').toDouble()
    }
}



/*http://www.cbr.ru/scripts/XML_daily.asp
    <?xml version="1.0" encoding="windows-1251"?>
<ValCurs Date="11.01.2022" name="Foreign Currency Market">
    <Valute ID="R01010">
        <NumCode>036</NumCode>
        <CharCode>AUD</CharCode>
        <Nominal>1</Nominal>
        <Name>Австралийский доллар</Name>
        <Value>53,9820</Value>
    </Valute>
</ValCurs>*/