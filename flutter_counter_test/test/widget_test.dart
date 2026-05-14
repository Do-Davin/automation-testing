import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_counter_test/main.dart';

void main() {
  testWidgets('counter increments and decrements', (WidgetTester tester) async {
    await tester.pumpWidget(const CounterApp());

    expect(find.text('0'), findsOneWidget);

    await tester.tap(find.byKey(const Key('incrementButton')));
    await tester.pump();

    expect(find.text('1'), findsOneWidget);

    await tester.tap(find.byKey(const Key('decrementButton')));
    await tester.pump();

    expect(find.text('0'), findsOneWidget);
  });
}

