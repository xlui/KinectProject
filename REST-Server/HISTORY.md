# Develop History

## 0.2.1 (2017-11-12)

feature:

- API `train`, get result of train throught HTTP POST, also provides HTTP GET to read results. 

bugfix:

- fix logical of API `latest` `update` `history` and `latest_picture`

## 0.2.0 (2017-11-12)

refactor:

- devastating refactor, change some API and optimize code structure

## 0.1.4 (2017-11-05)

feature:

- API `latest_picture`

## 0.1.3 (2017-11-04)

feature:

- log support

## 0.1.2 (2017-11-04)

features:

- Enrich command line for showing database content
- use flask log in `wsgi.py`

## 0.1.1 (2017-11-04)

feature:

- API `latest` now return a variable `danger` to indicate the state's degree of risk

## 0.1.0 (2017-11-04)

forget to write history before, write from now

dependencies:

- alembic==0.9.6
- click==6.7
- Flask==0.12.2
- Flask-HTTPAuth==3.2.3
- Flask-Login==0.4.0
- Flask-Migrate==2.1.1
- Flask-Script==2.0.6
- Flask-SQLAlchemy==2.3.2
- Flask-Uploads==0.2.1
- itsdangerous==0.24
- Jinja2==2.9.6
- Mako==1.0.7
- MarkupSafe==1.0
- python-dateutil==2.6.1
- python-editor==1.0.3
- six==1.11.0
- SQLAlchemy==1.1.14
- Werkzeug==0.12.2
