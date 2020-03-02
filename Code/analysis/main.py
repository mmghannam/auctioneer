import pandas as pd
import plotly.express as px

colnames = ["BidderId", "name", "day", "budget", "value", "bid", "utility", "payment", "rank"]
df = pd.read_csv('x.csv', names=colnames)
# df = df[df.BidderId != 29]
# df = df[df.BidderId != 13]

fig = px.scatter(df, x=df.day, y=df.bid, color=df.BidderId, hover_data=[df.name,
                                                                     df.value,
                                                                     df.payment,
                                                                     df.utility],
              marginal_y="rug",
              marginal_x="histogram")
fig.show()
