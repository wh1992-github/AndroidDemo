---
name: "Stock File Analysis"
description: "Analyze a stock history file and decide whether the latest date is a buy point or sell point"
category: Analysis
tags: [stock, finance, file, analysis]
---

Analyze the stock history file provided after `/stock-file-analysis`.

**Input**: A local file path, uploaded file reference, or plain description of the stock data source.

Use the workflow defined in:
- `skills/stock-file-analysis/SKILL.md`

## Required behavior

1. Read the file and detect the latest valid trading date.
2. Compute buy-point and sell-point checks using the latest close as the anchor.
3. Default to checking `n=10..15` unless the user gives a different range.
4. Default to using the `2%~8%` range for buy/sell candidates unless the user overrides it.
5. Always combine:
   - recent low/high close analysis
   - moving-average trend
   - recent volume comparison
6. Give a direct judgment:
   - `BUY`
   - `SELL`
   - `WAIT`
7. Explain the judgment with concrete values from the file.

## Output format

Use a concise, decision-first structure:

```text
Latest date: YYYY-MM-DD
Latest close: X.XXX

Buy-point analysis:
- ...

Sell-point analysis:
- ...

Trend and volume:
- ...

Final judgment:
- BUY / SELL / WAIT
- Reasons: ...
```

## Important constraints

- If the latest row in the file is not today's date, explicitly state the exact latest date.
- If the file is being edited or locked by another process, retry with shared read access if possible.
- Do not answer with generic market commentary.
- Do not omit the final buy/sell/wait decision.
