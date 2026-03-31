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

Prefer files that contain at least:
- date
- close price
- change percent
- amplitude
- volume or total hands

Optional but useful:
- open
- high
- low
- amount

If the file is missing a required field, say exactly what is missing and stop.

## Analysis Workflow

1. Read the file from the user-provided path or uploaded content.
2. Detect the latest valid trading row.
3. Use the latest close as the anchor.
4. Compute buy-point and sell-point checks from historical closes.
5. Combine price action, volume, and trend.
6. Give a direct judgment with reasons.

## Buy/Sell Logic

Default buy/sell point calculation:

- Use the latest close as the anchor price.
- For each `n` in `10..15`:
  - find the lowest close in the previous `n` trading days, excluding the latest day
  - compute rise from that low to the latest close
  - treat this as a buy-point candidate
  - find the highest close in the previous `n` trading days, excluding the latest day
  - compute drop from that high to the latest close
  - treat this as a sell-point candidate

Default threshold guidance:
- `2% ~ 8%` rise from recent low: buy-point range
- `2% ~ 8%` drop from recent high: sell-point range

If the user gives a different `n` or threshold, use the user's version instead.

## Trend And Volume Checks

Always add context from trend and volume:

- compute recent moving averages when enough rows exist:
  - `MA5`
  - `MA10`
  - `MA20`
- compare latest close against these moving averages
- compare latest volume against:
  - 5-day average volume
  - 10-day average volume
  - 20-day average volume

Interpretation rules:

- If price is above `MA5/MA10/MA20`, trend is relatively strong.
- If price is below `MA5/MA10/MA20`, trend is relatively weak.
- If latest volume is clearly above recent averages, mention expansion.
- If latest volume is below recent averages, mention weak confirmation.
- If the price matches a buy-point range but trend is still weak, downgrade the buy conclusion.
- If the price matches a sell-point range but the stock has already fallen well beyond the range, say it is no longer an ideal sell point.

## Output Format

Keep the answer direct and decision-oriented.

Preferred structure:

```text
Latest date: YYYY-MM-DD
Latest close: X.XXX

Buy-point analysis:
- n=10: ...
- n=11: ...
...

Sell-point analysis:
- n=10: ...
- n=11: ...
...

Trend and volume:
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
