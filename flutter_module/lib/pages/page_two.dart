import 'package:flutter/material.dart';

/// 页面二
class PageTwo extends StatefulWidget {
  const PageTwo({super.key});

  @override
  State<PageTwo> createState() => _PageTwoState();
}

class _PageTwoState extends State<PageTwo> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter += 2; // 每次加 2
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('页面二'),
        backgroundColor: Colors.green,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Icon(
              Icons.looks_two,
              size: 100,
              color: Colors.green,
            ),
            const SizedBox(height: 20),
            const Text(
              '这是 Flutter 页面二',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            const Text(
              '点击次数统计（每次+2）:',
              style: TextStyle(fontSize: 16),
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '增加 2',
        backgroundColor: Colors.green,
        child: const Icon(Icons.add),
      ),
    );
  }
}
