import pandas as pd
import plotly.express as px
import seaborn as sns
import matplotlib.pyplot as plt
import plotly.graph_objects as go
import pyarrow

colnames = ["BidderId", "name", "day", "budget", "value", "bid", "utility", "payment", "rank"]
df = pd.read_feather('trail_data.feather')


def plot_all_bidding_profiles():
    fig = px.scatter(df, x=df.day, y=df.bid, color=df.name, hover_data=[df.name,
                                                                        df.value,
                                                                        df.payment,
                                                                        df.utility])
    fig.show()


def plot_individual_bidding_profiles():
    names = df.name.unique()
    for name in names:
        df_for_name = df[df.name == name]
        fig = go.Figure()
        # value trace
        fig.add_trace(
            go.Scatter(x=df_for_name.day, y=df_for_name.value, mode="markers", name="value", marker={"opacity": 0.3})
        )

        # bid trace
        fig.add_trace(
            go.Scatter(x=df_for_name.day, y=df_for_name.bid, mode="markers", name="bid", marker={"opacity": 0.5})
        )

        # utility trace
        fig.add_trace(
            go.Scatter(x=df_for_name.day, y=df_for_name.utility, mode="lines", name="utility", marker={"opacity": 0.7})
        )

        fig.update_layout(
            title=name,
            xaxis_title="day",
            yaxis_title="amount",
            font=dict(
                family="Courier New, monospace",
                size=14,
                color="#7f7f7f"
            )
        )

        fig.write_image(f"plots/{name}.png", scale=2)
        print(name)


if __name__ == '__main__':
    plot_individual_bidding_profiles()
