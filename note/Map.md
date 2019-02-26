Map 集合类用于存储元素对（称作“键”和“值”），其中每个键映射到一个值。Map是一个Key到Value的映射，通过Key可以获取到value。

Map的key不能重复，一个Key最多映射到一个value。  
Dictionary是一个抽象类，Map接口取代了Dictionary。  
Map提供了3个集合视图：
* 所有Key的集合，参考keyset()；  
* 所有value的集合，参考values()；  
* key-value的映射，参考entrySet()。

映射顺序定义为迭代器在映射的collection视图上返回其元素的顺序。某些映射实现可明确保证其顺序，如TreeMap类；另一些映射实现则不保证顺序，如hashMap类。

注：将可变对象用作映射键时必须格外小心。当对象是映射中某个键时，如果以影响equals比较的方式更改了对象的值，则映射的行为将是不确定的。此项禁止的一种特殊情况是不允许某个映射将自身作为一个键包含。虽然允许某个映射将自身作为值包含，但请格外小心：在这样的映射上equals和hashCode方法的定义将不再是明确的。

![](/imgs/java_collection_uml.png)

