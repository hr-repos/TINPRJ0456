FROM    caddy:2.7.4

RUN     mkdir -p /var/www/html
COPY    dist /var/www/html
COPY    Caddyfile /etc/caddy/Caddyfile

EXPOSE  80
CMD     caddy run --config /etc/caddy/Caddyfile
