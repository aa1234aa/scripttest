/*在进行测试数据生成操作之前，需要将脚本放在E:\测试数据初始化\2.3.1版本的目录下，才能进行下一步操作*/

1.获取被测环境Web地址的账号和密码，登陆Web后获取Cookie信息的CSRF和R_SESS。
2.进入E:\测试数据初始化\2.3.1版本\Resources\Conf目录，打开basicdata.properties文件，配置CSRF，R_SESS，server_ip和server_port的信息（为被测环境的cookie信息和Web地址的访问路径和端口号。
3.获取被测环境Web数据库的url，端口号，账号，密码还有数据库名，首先确定本地是否能连接被测环境的数据库，如可以，则进行第下一步。
4.确保被测环境的数据库可用后，需要打个数据库引用jar包，比如我给61测试环境打包成为Util(61).jar，放到Jmeter的\apache-jmeter-4.0\lib\ext目录，此目录只能允许有一个Util(xx).jar包，不允许存在多个不同的包或者相同的包。
5.最后就是本地ant运行E:\测试数据初始化\2.3.1版本\Resources\Conf目录的build.xml配置文件，即可自动对被测环境进行测试数据生成；