import logging
from logging.handlers import RotatingFileHandler

from app import create_app

app = create_app()
handler = RotatingFileHandler('/home/liuqi/record.log', maxBytes=10000, backupCount=1)
app.logger.setLevel(logging.DEBUG)
app.logger.addHandler(handler)


@app.route('/test_log')
def test():
    app.logger.debug("debug log")
    app.logger.info("info log")
    app.logger.warning("warning log")
    app.logger.error("error log")
    app.logger.critical("critical log")
    return "Hello"


if __name__ == '__main__':
    app.run()
