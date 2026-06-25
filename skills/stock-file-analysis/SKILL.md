---
name: stock-file-analysis
description: Analyze a user-provided stock history file and return the latest analysis result, whether the latest date is a buy point or sell point, and the concrete reasons. Use when the user gives a stock data file path, uploads a stock history table, or asks you to judge if today is a buy point or sell point from historical price and volume data.
---

# Stock File Analysis

Use this skill when the user provides a stock history file and wants a direct judgment, not a generic explanation.

The expected output is:
- latest available trading date
- latest close price
- buy-point check
- sell-point check
- trend and volume interpretation
- final judgment: `BUY`, `SELL`, or `WAIT`
- concise reasons tied to the data

## File Expectations

The project's data files live in `app/src/main/assets/` and use this exact format, one row per trading day:

```text
YYYY-MM-DD,weekday------close
2016-01-04,一------20.78
```

- The price is the text after `------` (6 characters, parsed as float).
- Weekday is a single Chinese character (一/二/三/四/五/六/日).
- These files contain **only date, weekday, and close price** — no change percent, amplitude, or volume.

Generic user-provided CSV files with extra columns (open/high/low/volume) are also acceptable. If a requested check needs a field the file does not have (e.g. volume), say so explicitly and skip that check instead of fabricating values.

## Analysis Workflow

1. Read the file from the user-provided path or uploaded content.
2. Detect the latest valid trading row.
3. Use the latest close as the anchor.
4. Compute buy-point and sell-point checks from historical closes.
5. Combine price action, volume, and trend.
6. Give a direct judgment with reasons.

## Buy/Sell Logic

Default buy/sell point calculation (aligned with `app/src/main/java/com/example/helloworld/JDK.java`):

- Use the latest close as the anchor price.
- Use a fixed window of `n = 10` trading days (`int day = 10` in JDK.java) to track the local lowest/highest close.
- Compute the rise from the tracked low to the latest close (buy-point candidate) and the drop from the tracked high (sell-point candidate).
- The project's backtest searches thresholds from `2%` to `6%` in `0.2%` steps (loop 200..600 step 20), with a 240-day cycle, 480-day window, initial capital 10000 and a 15-unit fee per sell.

Default threshold guidance:
- `2% ~ 6%` rise from recent low: buy-point range
- `2% ~ 6%` drop from recent high: sell-point range

If the user gives a different `n` or threshold, use the user's version instead.

## Trend And Volume Checks

Add trend context when enough rows exist:

- compute recent moving averages: `MA5`, `MA10`, `MA20`
- compare the latest close against these moving averages

Volume checks apply **only if the input file actually contains a volume column**. The project's own `assets/*.txt` files do not, so for those files skip all volume checks and state that volume data is unavailable.

Interpretation rules:

- If price is above `MA5/MA10/MA20`, trend is relatively strong.
- If price is below `MA5/MA10/MA20`, trend is relatively weak.
- If volume is available and clearly above recent averages, mention expansion; if below, mention weak confirmation.
- If the price matches a buy-point range but trend is still weak, downgrade the buy conclusion.
- If the price matches a sell-point range but the stock has already fallen well beyond the range, say it is no longer an ideal sell point.

## Output Format

Keep the answer direct and decision-oriented.

Preferred structure:

```text
Latest date: YYYY-MM-DD
Latest close: X.XXX

Buy-point analysis:
- tracked 10-day low: ...
- rise from low: ...
- threshold in 2%~6%: ...

Sell-point analysis:
- tracked 10-day high: ...
- drop from high: ...
- threshold in 2%~6%: ...

Trend (and volume if available):
- ...

Final judgment:
- BUY / SELL / WAIT
- Reasons: ...
```

## Judgment Rules

Use `BUY` when:
- at least one buy-point candidate falls in range
- and trend/volume do not strongly contradict it

Use `SELL` when:
- at least one sell-point candidate falls in range
- and trend/volume do not strongly contradict it

Use `WAIT` when:
- no buy-point or sell-point candidate is valid
- or the signal exists but trend/volume conflict is too strong

If both buy and sell candidates appear at once, explain the conflict and prefer `WAIT` unless one side is clearly stronger.

## Important Constraints

- Do not answer with only formulas.
- Do not give a vague market commentary without a decision.
- Tie every conclusion to concrete values from the file.
- If the latest date in the file is not actually today, explicitly say so and use the exact latest date from the file.
