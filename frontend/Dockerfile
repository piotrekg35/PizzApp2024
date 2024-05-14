FROM node:18-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY .. .
RUN npm run build

# Serve the angular app with Nginx
FROM nginx:alpine
COPY --from=build-stage /app/dist/catering /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
