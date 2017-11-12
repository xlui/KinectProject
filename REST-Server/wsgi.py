from app import create_app

app = create_app()


# @app.route('/test_log')
# def test():
#     app.logger.debug("debug log")
#     app.logger.info("info log")
#     app.logger.warning("warning log")
#     app.logger.error("error log")
#     app.logger.critical("critical log")
#     return "Hello"


if __name__ == '__main__':
    app.run()
