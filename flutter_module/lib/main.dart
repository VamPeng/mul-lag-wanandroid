import 'package:flutter/material.dart';
import 'pages/page_one.dart';
import 'pages/page_two.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Module',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      // 配置命名路由
      routes: {
        '/': (context) => const MyHomePage(title: 'Flutter 首页'),
        '/page_one': (context) => const PageOne(),
        '/page_two': (context) => const PageTwo(),
      },
      // 默认首页
      initialRoute: '/',
    );
  }
}

/// 默认首页
class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        backgroundColor: Colors.purple,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Icon(
              Icons.home,
              size: 100,
              color: Colors.purple,
            ),
            const SizedBox(height: 20),
            const Text(
              '欢迎来到 Flutter 模块！',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            const Text(
              '点击次数统计:',
              style: TextStyle(fontSize: 16),
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            const SizedBox(height: 40),
            // 导航按钮
            ElevatedButton.icon(
              onPressed: () {
                Navigator.pushNamed(context, '/page_one');
              },
              icon: const Icon(Icons.looks_one),
              label: const Text('前往页面一'),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blue,
                padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
              ),
            ),
            const SizedBox(height: 10),
            ElevatedButton.icon(
              onPressed: () {
                Navigator.pushNamed(context, '/page_two');
              },
              icon: const Icon(Icons.looks_two),
              label: const Text('前往页面二'),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green,
                padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '增加',
        backgroundColor: Colors.purple,
        child: const Icon(Icons.add),
      ),
    );
  }
}
