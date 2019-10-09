package com.sheng.albumselector.utils

//排序器
class AlbumComparator(private val allText: String) : Comparator<String> {

    override fun compare(s1: String, s2: String): Int {
        if (s1.equals(allText))
            return -1
        else if (s2.equals(allText))
            return 1
        else {
            var c1 = s1.get(0)
            var c2 = s2.get(0)
            val value = c2 - c1
            if (Math.abs(value) == 32)//说明两者之间有大小写关系
                return value
            //没有大小写关系
            c1 = c1.toUpperCase()
            c2 = c2.toUpperCase()
            return c1 - c2
        }
    }

}