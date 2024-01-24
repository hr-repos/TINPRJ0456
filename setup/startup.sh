tmux new-session -d -s back-end
tmux send-keys -t back-end "./startup-api.sh" C-m

tmux new-session -d -s gpio
tmux send-keys -t gpio "./startup-gpio.sh" C-m

tmux new-session -d -s sql
tmux send-keys -t sql "sudo mysql" C-m
