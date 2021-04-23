package com.f2prateek.rx.preferences2

internal class PointPreferenceConverter : Preference.Converter<Point> {
    override fun deserialize(serialized: String): Point {
        val parts = serialized.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        check(parts.size == 2) { "Malformed point value: '$serialized'" }
        return Point(parts[0].toInt(), parts[1].toInt())
    }

    override fun serialize(value: Point): String {
        return value.x.toString() + "," + value.y
    }
}