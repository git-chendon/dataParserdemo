utf-8 使用1-4字节为每个字符编码：
1、US-Ascll字符只需要1字节编码。
2、带有变音符号的拉丁文、希腊文、西里尔字母、亚美尼亚语、希伯来文、阿拉伯文、叙利亚文等字母则需要2字节编码（Unicode范围由U+0080~U+07FF）。
3、其他语言的字符（包括中日韩文字、东南亚文字、中东文字等）包含了大部分常用字，使用3字节编码。
4、其他极少使用的语言字符使用4字节编码。
https://baike.baidu.com/item/UTF-8  百度百科上面有utf-8转换表具体几位的定义。